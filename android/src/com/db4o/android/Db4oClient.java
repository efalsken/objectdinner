package com.db4o.android;

import com.androidstartup.view.AndroidND;
import com.androidstartup.view.R;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.android.Db4oService;
import com.db4o.android.ServerSetup;
import com.db4o.cs.Db4oClientServer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.query.Predicate;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Db4oClient extends Activity implements ServerSetup {
	
	private static final String TAG = null;
	public static ObjectContainer objectContainer = null;
	private Button mStartServiceButton;                                         
    private Button mStopServiceButton;  
    
    public void onCreate(Bundle savedInstanceState)                             
    {                                                                                                       
        super.onCreate(savedInstanceState);                                     
        setContentView(R.layout.db4oclient);                               
                                                                                
        // Obtain handles to UI objects                                              
        mStartServiceButton = (Button) findViewById(R.id.startServiceButton);   
        mStopServiceButton = (Button) findViewById(R.id.stopServiceButton);                        
                                                                                
        // Register handlers for UI elements                                                                                                                                                                                  
        mStartServiceButton.setOnClickListener(new View.OnClickListener() {     
            public void onClick(View v) {                                       
                Log.d(TAG, "mStartServiceButton clicked");                      
                doBindService();                                                
            }                                                                   
        });                                                                     
                                                                                
        mStopServiceButton.setOnClickListener(new View.OnClickListener() {      
            public void onClick(View v) {                                       
                Log.d(TAG, "mStopServiceButton clicked");                       
                doUnbindService();
            }                                                                   
        });                                                                                                                                                                                        
    }                                                                           
                                                                                   
	
	/**
     * Service maintenance methods
     */
    
    /** Messenger for communicating with service. */
    Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    boolean mIsBound;
    /** Some text view we are using to show state information. */
    //TextView mCallbackText;
    
    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Db4oService.MSG_SET_VALUE:
                    //mCallbackText.setText("Received from service: " + msg.arg1);
                	openClient();
                	Intent result = new Intent();
                	if(objectContainer != null){
						result.putExtra(AndroidND.DB4OSERVICE, "OPENED");
						setResult(RESULT_OK, result);
                	} else{
                		result.putExtra(AndroidND.DB4OSERVICE, "FAILED");
						setResult(RESULT_OK, result);
                	}
					finish();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
            //mCallbackText.setText("Attached.");

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                		Db4oService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
                
                // Give it some value as an example.
                msg = Message.obtain(null,
                		Db4oService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
            
            // As part of the sample, tell the user what happened.
            Toast.makeText(Db4oClient.this, R.string.remote_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            //mCallbackText.setText("Disconnected.");

            // As part of the sample, tell the user what happened.
            Toast.makeText(Db4oClient.this, R.string.remote_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
    	Intent i = new Intent();
	    i.setClassName( "com.db4o.android", 
			"com.db4o.android.Db4oService" );
	    startService(i);
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        bindService(new Intent(Db4oClient.this, 
        		Db4oService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        //mCallbackText.setText("Binding.");
    }
    
    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            Db4oService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }
            
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            //mCallbackText.setText("Unbinding.");
            Intent i = new Intent();
    	    i.setClassName( "com.db4o.android", 
    			"com.db4o.android.Db4oService" );
    	    stopService(i);
            Intent result = new Intent();
			result.putExtra(AndroidND.DB4OSERVICE, "CLOSED");
			setResult(RESULT_OK, result);
			finish();
        }
    }
    
    private void openClient(){
	    try  {
	
	      // connect to the server
	      objectContainer = Db4oClientServer.openClient(Db4oClientServer
	          .newClientConfiguration(), HOST, PORT, USER, PASS);
	
	    } catch (Exception e)  {
	      e.printStackTrace();
	    }
	    	
	    /*if (objectContainer != null) {
	    	Dinner d = new Dinner("Dinner1");
		    objectContainer.store(d);
		    objectContainer.commit();
		    objectContainer.delete(d);
		    objectContainer.commit();
	      // close the ObjectContainer
	      objectContainer.close();
	      Toast.makeText(Db4oClient.this, "Saved and deleted object",
	                Toast.LENGTH_SHORT).show();
	    } else {
	    	Toast.makeText(Db4oClient.this, "ObjectContainer = null!!",
	                Toast.LENGTH_SHORT).show();
	    }*/
    }
    
	public void store(Object o) throws DatabaseClosedException,
			DatabaseReadOnlyException {
		
		objectContainer.store(o);
	}

	public <TargetType> ObjectSet<TargetType> query(
			Class<TargetType> targetTypeClass) throws Db4oIOException,
			DatabaseClosedException {

		return objectContainer.query(targetTypeClass);

	}

	public <TargetType> ObjectSet<TargetType> query(
			Predicate<TargetType> targetTypePredicate) throws Db4oIOException,
			DatabaseClosedException {

		return objectContainer.query(targetTypePredicate);

	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		doUnbindService();
	}
}
