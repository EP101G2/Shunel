package com.ed.shunel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.ChatImageView;
import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static com.ed.shunel.CommonTwo.chatWebSocketClient;
import static com.ed.shunel.CommonTwo.loadUserName;
import static com.ed.shunel.CommonTwo.showToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "TAG_ChatFragment";
    private Activity activity;
    private LocalBroadcastManager broadcastManager;
    private TextView tvMessage;
    private EditText etMessage;
    private ScrollView scrollView;
    private Button btSend;
    private RecyclerView rv;
    private String friend;
    private CommonTask chatTask, messageTask;
    private String user_ID;
    private String user_Name;
    private int chat_ID;

    /*拍照元件*/
    private CardView cv_Picture;
    private Button btnTakePicture;
    private Button btnPickPicture;
    private Button btnPicture;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;
    private byte[] image;
    private String base61ToStr;
    private ChatImageView imageTask;
    private int imageID;
    Bitmap bitmap = null;
    private ChatMessage chatMessage = null;
    String message = "";
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        chat_ID = bundle.getInt("chatroom");

        // Inflate the layout for this fragment
        friend = "Shunel";
        user_ID = Common.getPreherences(activity).getString("id", "");
        user_Name = Common.getPreherences(activity).getString("name", "");
        Log.e(TAG,user_ID);
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(activity);
        registerChatReceiver();
        chatMessageList = getData();
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();


    }

    private void findViews(View view) {
        rv = view.findViewById(R.id.rv);
        btSend = view.findViewById(R.id.btSend);
        etMessage = view.findViewById(R.id.etMessage);

        /*拍照元件*/
        cv_Picture = view.findViewById(R.id.cv_Picture);
        btnTakePicture = view.findViewById(R.id.btnTakePicture);
        btnPickPicture = view.findViewById(R.id.btnPickPicture);
        btnPicture = view.findViewById(R.id.btnPicture);
        cv_Picture.setVisibility(View.GONE);

    }

    private void initData() {


    }

    private List<ChatMessage> getData() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<ChatMessage> messages = null;

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Chat_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "getAll");
            jsonObject.addProperty("chat_ID", chat_ID);
            messageTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = messageTask.execute().get();
                Type listType = new TypeToken<List<ChatMessage>>() {
                }.getType();
                messages = gson.fromJson(jsonIn, listType);

                if (messages !=null){
                    for (ChatMessage chat : messages) {
                        if (chat.getType().equals("image")) {
                            chat.setBase64(String.valueOf(chat.getId()));
                            chat.setFlag(1);
                            Log.e("3123", "===============================" + chat.getBase64());
                        }
                    }
                }else {
                    return null;
                }



            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("--------------", messages + "");

        return messages;
    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.setAdapter(new messageFragment(activity, chatMessageList));

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etMessage.getText().toString();
                if (message.trim().isEmpty()) {
                    Toast.makeText(activity, "請輸入文字", Toast.LENGTH_LONG).show();
                    return;
                }
                String sender = loadUserName(activity);
                // 將欲傳送訊息轉成JSON後送出
                if (image != null) {
                    chatMessage = new ChatMessage("image", user_ID, friend, message, chat_ID, base61ToStr, 0);
                    image = null;
                } else {
                    chatMessage = new ChatMessage("chat", user_ID, friend, message, chat_ID);
                }

                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocketClient.send(chatMessageJson);
                Log.d("btSend:", "output: " + chatMessageJson);
                // 將欲傳送訊息顯示在TextView上
                chatMessageList.add(chatMessage);
                // 將輸入的訊息清空
//                sendChatDB(chatMessage);
                messageFragment adpter = (messageFragment) rv.getAdapter();
                if (adpter != null) {
                    adpter.setListforMsg(chatMessageList);
                    adpter.notifyDataSetChanged();
                }
                etMessage.setText(null);


            }
        });



        /*拍照*/
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cardview = true;

                if (cardview) {
                    cv_Picture.setVisibility(View.VISIBLE);
                    cardview = false;
                } else {
                    cv_Picture.setVisibility(View.GONE);
                    cardview = true;
                }
            }
        });

        btnPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_PICTURE);

                etMessage.setText("請點選送出");
            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                    etMessage.setText("請點選送出");
                } else {
                    Common.showToast(activity, R.string.textNoCameraApp);
                }
            }
        });


    }



    /**
     * 註冊廣播接收器攔截聊天資訊
     * 因為是在Fragment註冊，所以Fragment頁面未開時不會攔截廣播
     */
    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
    }

    // 接收到聊天訊息會在TextView呈現
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            String sender = chatMessage.getSender();
            chatMessage.setFlag(1);
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView
            chatMessageList.add(chatMessage);
            messageFragment adpter = (messageFragment) rv.getAdapter();
            if (adpter != null) {
                adpter.setListforMsg(chatMessageList);
                adpter.notifyDataSetChanged();
            }
            Log.d("=============", message);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Fragment頁面切換時解除註冊，但不需要關閉WebSocket，
        // 否則回到前頁好友列表，會因為斷線而無法顯示好友
        broadcastManager.unregisterReceiver(chatReceiver);
    }


    private class messageFragment extends RecyclerView.Adapter<messageFragment.MyViewholder> {
        /*layout分別是自己發送的文字內容，自己發送的圖片，與別人發的文字和圖片*/
        private final int TYPE_MESSAGE_SENT = 0;
        private final int TYPE_MESSAGE_RECEIVED = 1;
        private final int TYPE_IMAGE_SENT = 2;
        private final int TYPE_IMAGE_RECEIVED = 3;

        private int imageSize;


        private LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context context;


        List<ChatMessage> message;

        public messageFragment(Context context, List<ChatMessage> message) {
            this.context = context;
            this.message = message;

            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setListforMsg(List<ChatMessage> message) {
            this.message = message;
        }


        @NonNull
        @Override
        public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;

            switch (viewType) {
                case TYPE_MESSAGE_SENT:
                    itemView = inflater.inflate(R.layout.their_message, parent, false);
                    return new SentMessageHolder(itemView);

                case TYPE_MESSAGE_RECEIVED:
                    itemView = inflater.inflate(R.layout.my_message, parent, false);
                    return new ReceivedMessageHolder(itemView);

                case TYPE_IMAGE_SENT:
                    itemView = inflater.inflate(R.layout.item_received_photo, parent, false);
                    return new SentImageHolder(itemView);

                case TYPE_IMAGE_RECEIVED:
                    itemView = inflater.inflate(R.layout.item_sent_image, parent, false);
                    return new ReceivedImageHolder(itemView);
                default:
                    throw new IllegalStateException("Unexpected value: " + viewType);
            }
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage CM = message.get(position);

            if (CM.getSender().equals("Shunel")) {
                if (CM.getType().equals("chat")) {
                    return TYPE_MESSAGE_SENT;
                } else {
                    return TYPE_IMAGE_SENT;
                }
            } else {
                if (CM.getType().equals("chat")) {
                    return TYPE_MESSAGE_RECEIVED;
                } else {
                    return TYPE_IMAGE_RECEIVED;
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder holder, int position) {



            final ChatMessage CM = message.get(position);

            Calendar mCal = Calendar.getInstance();
            CharSequence s = DateFormat.format("hh:mm:ss", mCal.getTime());


//

            //判斷發送的人是誰
            if (CM.getSender().equals(Common.getPreherences(activity).getString("id", ""))) {

                //判斷訊息是chat or image
                if (CM.getType().equals("chat")) {
                    ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
                    receivedMessageHolder.messageTxt.setText(CM.getMessage());

                    //判斷時間是否有null
                    if (CM.getDate() != null) {
                        receivedMessageHolder.messageTime.setText(DateToStr(CM.getDate()));
                    } else {
                        receivedMessageHolder.messageTime.setText(s);
                    }

                    //訊息為image
                } else {
                    ReceivedImageHolder receivedImageHolder = (ReceivedImageHolder) holder;
                    //判斷訊息是誰發的 1表示自己
                    if (CM.getFlag() == 1) {
                        //去db抓資料
                        String url = Common.URL_SERVER + "Chat_Servlet";
                        imageTask = new ChatImageView(url, Integer.parseInt(CM.getBase64()), imageSize, ((ReceivedImageHolder) holder).imageView);
                        imageTask.execute();

                        //判斷時間是否有null
                        if (CM.getDate() != null) {
                            receivedImageHolder.tvTheirImage.setText(DateToStr(CM.getDate()));
                        } else {
                            receivedImageHolder.tvTheirImage.setText(s);
                        }

                    } else {
                        //即時發送的訊息
                        receivedImageHolder.imageView.setImageBitmap(bitmap);
                        //判斷時間是否有null
                        if (CM.getDate() != null) {
                            receivedImageHolder.tvTheirImage.setText(DateToStr(CM.getDate()));
                        } else {
                            receivedImageHolder.tvTheirImage.setText(s);
                        }
                    }
                }


            } else {

                if (CM.getType().equals("chat")) {
                    SentMessageHolder sentMessageHolder = (SentMessageHolder) holder;
                    sentMessageHolder.nameTxt.setText(CM.getSender());
                    sentMessageHolder.messageTxt.setText(CM.getMessage());
                    if (CM.getDate() != null) {
                        sentMessageHolder.theirTime.setText(DateToStr(CM.getDate()));
                    } else {
                        sentMessageHolder.theirTime.setText(s);
                    }
                } else {
                    final SentImageHolder sentImageHolder = (SentImageHolder) holder;
                    String url = Common.URL_SERVER + "Chat_Servlet";

                    imageTask = new ChatImageView(url, Integer.parseInt(CM.getBase64()), imageSize, ((SentImageHolder) holder).imageView);
                    imageTask.execute();

                    if (CM.getDate() != null) {
                        sentImageHolder.tvMyImage.setText(s);
                    } else {
                        sentImageHolder.tvMyImage.setText(s);
                    }

                    sentImageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           sentImageHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                    });


                }


            }


        }

        @Override
        public int getItemCount() {
            return message == null ? 0 : message.size();
        }

        private class MyViewholder extends RecyclerView.ViewHolder {
            public MyViewholder(View itemView) {
                super(itemView);
            }
        }

        private class SentMessageHolder extends MyViewholder {

            TextView nameTxt, messageTxt, theirTime;

            public SentMessageHolder(@NonNull View itemView) {
                super(itemView);
                theirTime = itemView.findViewById(R.id.theirTime);
                nameTxt = itemView.findViewById(R.id.name);
                messageTxt = itemView.findViewById(R.id.message_mybody);
            }


        }

        private class ReceivedMessageHolder extends MyViewholder {
            TextView messageTxt, messageTime;

            public ReceivedMessageHolder(@NonNull View itemView) {
                super(itemView);
                messageTxt = itemView.findViewById(R.id.message_mybody);
                messageTime = itemView.findViewById(R.id.myTime);
            }
        }

        private class SentImageHolder extends MyViewholder {
            ImageView imageView;
            TextView tvMyImage;

            public SentImageHolder(@NonNull View itemView) {
                super(itemView);
                tvMyImage = itemView.findViewById(R.id.tvChat);
                imageView = itemView.findViewById(R.id.imageView_chat);
            }
        }

        private class ReceivedImageHolder extends MyViewholder {
            ImageView imageView;
            TextView nameTxt;
            TextView tvTheirImage;

            public ReceivedImageHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView_chat);
                nameTxt = itemView.findViewById(R.id.nameTxt);
                tvTheirImage = itemView.findViewById(R.id.tvChat);
            }
        }
    }

    /*時間轉換字串*/
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /*拍照功能*/
    private void handleCropResult(Intent intent) {
        ChatMessage chatMessage = null;
        Uri resultUri = UCrop.getOutput(intent);
        if (resultUri == null) {
            return;
        }

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                bitmap = BitmapFactory.decodeStream(
                        activity.getContentResolver().openInputStream(resultUri));
            } else {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            /*btmapToStr*/
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();

            base61ToStr = Base64.encodeToString(image, Base64.DEFAULT);

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .start(activity, this, REQ_CROP_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;
                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;
            }
        }
    }

    private Bitmap getBitmapFromString(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    private Bitmap btyeToBitmap(byte[] image) {
        if (image.length != 0) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else {
            return null;
        }

    }


}
