package com.codeit.team_4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends AppCompatActivity implements AIListener {

    private SpeechRecognizer speechRecognizer;
    ChatView chatView;
    SharedPreferences prefs;
    private AIService aiService;
    private AIDataService aiDataService;
    private static final String CLIENT_ACCESS_TOKEN = "<Your Acces Key Token>";
    static User me, you;
    //private boolean semaphore = true;
    Bitmap bmp;
    //PendingIntent pendingIntent;

    String sendTo, sendAmount, mdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        prefs = this.getSharedPreferences(
                "userDetailsSharedPref", MODE_PRIVATE);

        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getApplicationContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,0);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float rmsdB) { }

            @Override
            public void onBufferReceived(byte[] buffer) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int error) { }

            @Override
            public void onResults(Bundle results) {
                String msg = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRightMessage(true)
                        .setMessageText(msg)
                        .hideIcon(true)
                        .build();
                //Set to chat view
                chatView.send(message);
                if (msg.contains("fixed deposit")) {
                    sendFaqMsg("A fixed deposit (FD) is a financial instrument provided by banks which provides investors with a higher rate of interest than a regular savings account, until the given maturity date. It may or may not require the creation of a separate account.");
                } else if (msg.contains("cheque")) {
                    sendFaqMsg("It is written by an individual to transfer amount between two accounts of the same bank or a different bank and the money is withdrawn form the account.");
                } else if (msg.contains("demat account") || msg.contains("Demat account")) {
                    sendFaqMsg("The way in which a bank keeps money in a deposit account in the same way the Depository company converts share certificates into electronic form and keep them in a Demat account.");
                } else if (msg.contains("e banking")) {
                    sendFaqMsg("It is a type of banking in which we can conduct financial transactions electronically. RTGS, Credit cards, Debit cards etc come under this category.");
                } else if (msg.contains("fiscal deficit")) {
                    sendFaqMsg("It is the amount of Funds borrowed by the government to meet the expenditures.");
                } else if (msg.contains("debit card")) {
                    sendFaqMsg("It is a card issued by the bank so the customers can withdraw their money from their account electronically.");
                } else if (msg.contains("electronic fund transfer") || msg.contains("Electronic fund transfer")) {
                    sendFaqMsg("In this we use Automatic teller machine, wire transfer and computers to move funds between different accounts in different or same bank.");
                } else if (msg.contains("initial public offering")) {
                    sendFaqMsg("It is the time when a company makes the first offering of the shares to the pubic.");
                } else if (msg.contains("leverage ratio")) {
                    sendFaqMsg("It is a financial ratio which gives us an idea or a measure of a company’s ability to meet its financial losses.");
                } else if (msg.contains("liquidity")) {
                    sendFaqMsg("It is the ability of converting an investment quickly into cash with no loss in value.");
                } else if (msg.contains("market capitalization") || msg.contains("market capitalisation")) {
                    sendFaqMsg("The product of the share price and number of the company’s outstanding ordinary shares.");
                } else if (msg.contains("mortgage")) {
                    sendFaqMsg("It is a kind of security which one offers for taking an advance or loan from someone.");
                } else if (msg.contains("mutual funds") || msg.contains("mutual fund")) {
                    sendFaqMsg("These are investment schemes. It pools money from various investors in order to purchase securities.");
                } else if (msg.contains("pass book") || msg.contains("passbook")) {
                    sendFaqMsg("It is a book where all the bank transactions are recorded.They are mainly issued to Current or Savings Bank account holders.");
                } else if (msg.contains("repo rate")) {
                    sendFaqMsg("Commercial banks borrow funds by the RBI if there is any shortage in the form of rupees. If this rate increases it becomes expensive to borrow money from RBI and vice versa.");
                } else if (msg.contains("savings bank") || msg.contains("saving bank")) {
                    sendFaqMsg("It is account of nominal interest which can only be used for personal purpose and which has some restrictions on withdrawal.");
                } else if (msg.contains("statutory liquidity")) {
                    sendFaqMsg("It is amount that a commercial bank should have before giving credits to its customers which should be either in the form of gold,money or bonds.");
                } else if (msg.contains("teller")) {
                    sendFaqMsg("He/she is a staff member of the bank who cashes cheques, accepts deposits and perform different banking services for the general mass.");
                } else if (msg.contains("universal banking")) {
                    sendFaqMsg("When financial institutions and banks undertake activities related to banking like investment, issue of debit and credit card etc then it is known as universal banking.");
                } else if (msg.contains("virtual banking")) {
                    sendFaqMsg("Internet banking is sometimes known as virtual banking. It is called so because it has no bricks and boundaries. It is controlled by the world wide web.");
                } else {
                    // if nothing from above then excecute api.ai calls
                    executeAI(msg);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });

        chatView = (ChatView) findViewById(R.id.chat_view);
        final AIConfiguration config = new AIConfiguration(CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aiDataService = new AIDataService(config);

        int myId = 0;
//User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
//User name
        String myName = "Me";

        int yourId = 1;
        Bitmap yourIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_1);
        String yourName = "Bot";

        me = new User(myId, myName, myIcon);
        you = new User(yourId, yourName, yourIcon);

     
        chatView.setInputTextHint("Let's talk");
        chatView.setOptionIcon(R.drawable.ic_action_mic);
        chatView.setSendTimeTextColor(Color.WHITE);

        chatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(intent);
                Log.d("a","Recognition Started");
            }
        });

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(chatView.getInputText())) {
                    Toast.makeText(getApplicationContext(), "Enter input", Toast.LENGTH_SHORT).show();
                } else {
                    //new message
                    Message message = new Message.Builder()
                            .setUser(me)
                            .setRightMessage(true)
                            .setMessageText(chatView.getInputText())
                            .hideIcon(true)
                            .build();
                    //Set to chat view
                    chatView.send(message);
                    String msg = chatView.getInputText();
                    if (msg.contains("fixed deposit")) {
                        sendFaqMsg("A fixed deposit (FD) is a financial instrument provided by banks which provides investors with a higher rate of interest than a regular savings account, until the given maturity date. It may or may not require the creation of a separate account.");
                    } else if (msg.contains("cheque")) {
                        sendFaqMsg("It is written by an individual to transfer amount between two accounts of the same bank or a different bank and the money is withdrawn form the account.");
                    } else if (msg.contains("demat account") || msg.contains("Demat account")) {
                        sendFaqMsg("The way in which a bank keeps money in a deposit account in the same way the Depository company converts share certificates into electronic form and keep them in a Demat account.");
                    } else if (msg.contains("e banking")) {
                        sendFaqMsg("It is a type of banking in which we can conduct financial transactions electronically. RTGS, Credit cards, Debit cards etc come under this category.");
                    } else if (msg.contains("fiscal deficit")) {
                        sendFaqMsg("It is the amount of Funds borrowed by the government to meet the expenditures.");
                    } else if (msg.contains("debit card")) {
                        sendFaqMsg("It is a card issued by the bank so the customers can withdraw their money from their account electronically.");
                    } else if (msg.contains("electronic fund transfer") || msg.contains("Electronic fund transfer")) {
                        sendFaqMsg("In this we use Automatic teller machine, wire transfer and computers to move funds between different accounts in different or same bank.");
                    } else if (msg.contains("initial public offering")) {
                        sendFaqMsg("It is the time when a company makes the first offering of the shares to the pubic.");
                    } else if (msg.contains("leverage ratio")) {
                        sendFaqMsg("It is a financial ratio which gives us an idea or a measure of a company’s ability to meet its financial losses.");
                    } else if (msg.contains("liquidity")) {
                        sendFaqMsg("It is the ability of converting an investment quickly into cash with no loss in value.");
                    } else if (msg.contains("market capitalization") || msg.contains("market capitalisation")) {
                        sendFaqMsg("The product of the share price and number of the company’s outstanding ordinary shares.");
                    } else if (msg.contains("mortgage")) {
                        sendFaqMsg("It is a kind of security which one offers for taking an advance or loan from someone.");
                    } else if (msg.contains("mutual funds") || msg.contains("mutual fund")) {
                        sendFaqMsg("These are investment schemes. It pools money from various investors in order to purchase securities.");
                    } else if (msg.contains("pass book") || msg.contains("passbook")) {
                        sendFaqMsg("It is a book where all the bank transactions are recorded.They are mainly issued to Current or Savings Bank account holders.");
                    } else if (msg.contains("repo rate")) {
                        sendFaqMsg("Commercial banks borrow funds by the RBI if there is any shortage in the form of rupees. If this rate increases it becomes expensive to borrow money from RBI and vice versa.");
                    } else if (msg.contains("savings bank") || msg.contains("saving bank")) {
                        sendFaqMsg("It is account of nominal interest which can only be used for personal purpose and which has some restrictions on withdrawal.");
                    } else if (msg.contains("statutory liquidity")) {
                        sendFaqMsg("It is amount that a commercial bank should have before giving credits to its customers which should be either in the form of gold,money or bonds.");
                    } else if (msg.contains("teller")) {
                        sendFaqMsg("He/she is a staff member of the bank who cashes cheques, accepts deposits and perform different banking services for the general mass.");
                    } else if (msg.contains("universal banking")) {
                        sendFaqMsg("When financial institutions and banks undertake activities related to banking like investment, issue of debit and credit card etc then it is known as universal banking.");
                    } else if (msg.contains("virtual banking")) {
                        sendFaqMsg("Internet banking is sometimes known as virtual banking. It is called so because it has no bricks and boundaries. It is controlled by the world wide web.");
                    } else {
                        // if nothing from above then excecute api.ai calls
                        executeAI(msg);
                    }
                }

                //Reset edit text
                chatView.setInputText("");
            }
        });
    }

    void executeAI(String message) {
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(message);
        //aiService.textRequest(aiRequest);
        //final AIResponse aiResponse = aiDataService.request(aiRequest);

        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    return aiDataService.request(aiRequest);
                } catch (AIServiceException e) {
                    Log.e("Response Error", e.getMessage());
                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(AIResponse response) {
                if (response != null) {
                    // process aiResponse here
                    Result result = response.getResult();

                    // Get parameters
                    String parameterString = "";
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }
                    }
                    Message message = new Message.Builder()
                            .setUser(you)
                            .setRightMessage(false)
                            /*.setMessageText("Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + parameterString + "\nResolvedQuery: " + result.getResolvedQuery()
                                    + "\nResult: " + result.getFulfillment().getSpeech())*/
                            .setMessageText(result.getFulfillment().getSpeech())
                            .hideIcon(true)
                            .build();
                    //Set to chat view
                    chatView.send(message);
                    executeCorrespondingTask(result);
                }
            }
        }.execute(aiRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void executeCorrespondingTask(Result result) {
        if (result.getFulfillment().getSpeech().contains("Checking your balance")) {
            RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
            StringRequest stringRequest = getBalance();
            requestQueue.add(stringRequest);
        }
        if (result.getFulfillment().getSpeech().equals("Sending Money...")) {
            HashMap<String, JsonElement> resultParameters = result.getParameters();
            for (Map.Entry<String, JsonElement> entry : resultParameters.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (key.equals("phone")) {
                    sendTo = value.toString();
                    Log.d("SendMney", sendTo);
                }
                if (key.equals("amount")) {
                    sendAmount = value.toString();
                    Log.d("SendMney", sendAmount);
                }
            }

            if (sendTo.length() < 10) {
                sendFaqMsg("Wrong phone number...unable to send");
            } else {
                sendTo = sendTo.replaceAll("\\s+","");
                sendTo = sendTo.substring(sendTo.length()-11,sendTo.length());
                RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
                StringRequest stringRequest = sendMoney();
                requestQueue.add(stringRequest);
            }
        }
        if (result.getFulfillment().getSpeech().equals("Getting Transaction Details:")) {
            HashMap<String, JsonElement> resultParameters = result.getParameters();
            for (Map.Entry<String, JsonElement> entry : resultParameters.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (key.equals("date-period")) {
                    mdate = value.toString();
                    Log.d("mdate", mdate);
                }
            }
            RequestQueue requestQueue = Volley.newRequestQueue(ChatActivity.this);
            StringRequest stringRequest = getStatement();
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onResult(ai.api.model.AIResponse response) { }

    @Override
    public void onError(ai.api.model.AIError error) {
        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAudioLevel(float level) { }

    @Override
    public void onListeningStarted() { }

    @Override
    public void onListeningCanceled() { }

    @Override
    public void onListeningFinished() { }

    void sendFaqMsg(String result) {
        Message message = new Message.Builder()
                .setUser(you)
                .setRightMessage(false)
                .setMessageText(result)
                .hideIcon(true)
                .build();
        //Set to chat view
        chatView.send(message);
    }

    StringRequest getBalance() {
        String SEND_URL = "http://team4codeit.000webhostapp.com/getBalance.php";

        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AccountFragment",response);
                sendFaqMsg("Your Balance is: "+response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error","error fetching details via volley");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("mobile",prefs.getString("phone","error"));
                Log.d("AccountFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }

    StringRequest sendMoney() {

        String SEND_URL = "http://team4codeit.000webhostapp.com/transaction.php";
        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("FundTransferFragment", response);
                if (response.contains("1")) {
                    Log.d("FundTransferFragment", "Successful");
                    sendFaqMsg("Money sent successfully...");
                } else if (response.equals("-1")) {
                    Log.d("FundTransferFragment", "Unsuccessful");
                    sendFaqMsg("Balance low..cannot transfer");
                } else if (response.equals("0")) {
                    Log.d("FundTransferFragment", "Unsuccessful");
                    sendFaqMsg("Unsuccessful..User does not exist.");
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "error fetching details via volley");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sName", prefs.getString("name", "error"));
                params.put("sPhone", prefs.getString("phone", "error"));
                Log.d("Sendtonum", sendTo);
                params.put("rPhone", sendTo);
                params.put("amount", sendAmount);
                params.put("description", "Sent from ChatBot");

                Log.d("ChatAmount", sendAmount);

                Log.d("StatementFragment", String.valueOf(prefs.getString("phone", "-1")));
                return params;
            }
        };
    }

    StringRequest getStatement() {
        String SEND_URL = "http://team4codeit.000webhostapp.com/dateBot.php";

        return new StringRequest(Request.Method.POST, SEND_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ServerResponse",response);
                sendFaqMsg(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error","error fetching details via volley");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String s1 = mdate.substring(0,mdate.indexOf('/'));
                String s2 = mdate.substring(mdate.indexOf('/'), mdate.length());
                Log.d("HI", s1 + "\n" + s2);
                params.put("mobile",prefs.getString("phone","error"));
                params.put("sDate", s1);
                params.put("eDate", s2);
                Log.d("AccountFragment",String.valueOf(prefs.getString("phone","-1")));
                return params;
            }
        };
    }
}
