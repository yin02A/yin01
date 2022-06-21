package tools;

import android.content.Context;
import android.widget.Toast;

public class ToastTools {
    public static void showTextToast(Context context,String text){
        if(context!=null&&text!=null){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
