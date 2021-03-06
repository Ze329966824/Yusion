package com.yusion.shanghai.yusion.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion.ui.main.mine.SettingsActivity;

import static com.yusion.shanghai.yusion.base.ActivityManager.finish;


public class PopupDialogUtil {
    private static Dialog dialog;
    private static Context mContext;
    private static OnOkClickListener onOkClickListener;
    private static OnCancelClickListener onCancelClickListener;

    public static void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        PopupDialogUtil.onOkClickListener = onOkClickListener;
    }

    public static void OnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        PopupDialogUtil.onCancelClickListener = onCancelClickListener;
    }


    /**
     * 暴露给外界okListener实现隐藏对话框的方法
     */
    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public static void showOneButtonDialog(Context context, String msg, OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);

        TextView mMessage = (TextView) view.findViewById(R.id.dialog_login_msg);
        mMessage.setText(msg);
        TextView mOK = (TextView) view.findViewById(R.id.dialog_login_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });


        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }

    public static void showOneButtonDialog4CompleteInfo(Context context, OnOkClickListener okclickListener, OnCancelClickListener cancelClickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.alert_dialog);

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_complete_info, null);
        TextView mOK = (TextView) view.findViewById(R.id.dialog_login_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okclickListener != null) {
                    okclickListener.onOkClick(dialog);
                }
            }
        });

        TextView mCancel = (TextView) view.findViewById(R.id.dialog_login_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelClickListener != null){
                    cancelClickListener.onCancelClick(dialog);
                }
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }

    public static void showOneButtonDialog(Context context, @LayoutRes int layout_id, String msg, OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);

        View view = LayoutInflater.from(mContext).inflate(layout_id, null);

        TextView mMessage = (TextView) view.findViewById(R.id.dialog_login_msg);
        mMessage.setText(msg);
        TextView mOK = (TextView) view.findViewById(R.id.dialog_login_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });


        dialog.setContentView(view);
        dialog.setCancelable(false);
        show();
    }

    public static void showOneButtonDialog(Context context, String title, String msg,
                                           OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_one_button, null);
        TextView mTitle = (TextView) view.findViewById(R.id.popup_dialog_title);
        mTitle.setText(title);
        TextView mMessage = (TextView) view.findViewById(R.id.popup_dialog_message);
        mMessage.setText(msg);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
//        mOK.setOnClickListener(clickListener);

        ImageView mCancel = (ImageView) view.findViewById(R.id.btn_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(view);


//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽
//        int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高


//        dialog.getWindow().getAttributes().width = 259;
//        dialog.getWindow().getAttributes().height = 259;
        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes()
        dialog.show();
    }

    public static void showTwoButtonsDialog(Context context, String content, String leftMsg, String rightMsg, OnOkClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(rightMsg);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(leftMsg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public static void showTwoButtonsDialog4Warning(Context context, OnCancelClickListener clickListener) {
        mContext = context;
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button_warning, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onCancelClick(dialog);
                }
            }
        });
        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        dialog.setContentView(view);
        dialog.setCancelable(false);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public static void showTwoButtonsDialog(Context context, int width, int height, OnOkClickListener clickListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().width = width;
        dialog.getWindow().getAttributes().height = height;
        show();
    }

    public static void showTwoButtonsDialog(Context context, String leftbtn, String rightbtn, String message, OnOkClickListener clickListener, OnCancelClickListener cancelListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightbtn);
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onCancelClick(dialog);
                }
            }
        });

        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(message);
        dialog.setContentView(view);
        dialog.setCancelable(false);

//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(view.getWidth(),view.getHeight());
//        dialog.addContentView(view, params);
//
//        dialog.getWindow().getAttributes().width = width;
//        dialog.getWindow().getAttributes().height = height;

        show();
    }


    public static void showTwoButtonsDialog(Context context, int width, int height, String leftbtn, String rightbtn, String message, OnOkClickListener clickListener, OnCancelClickListener cancelListener) {
        mContext = context;
//        if (dialog == null) {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
//        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_dialog_two_button, null);
        TextView mOK = (TextView) view.findViewById(R.id.popup_dialog_ok);
//        mOK.setOnClickListener(okListener);
        mOK.setText(leftbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onOkClick(dialog);
                }
            }
        });
        TextView mCancel = (TextView) view.findViewById(R.id.popup_dialog_cancel);
        mCancel.setText(rightbtn);
//        mCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onCancelClick(dialog);
                }
            }
        });

        TextView mMsg = (TextView) view.findViewById(R.id.popup_dialog_msg);
        mMsg.setText(message);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().width = width;
        dialog.getWindow().getAttributes().height = height;

        show();
    }

    public interface OnOkClickListener {
        void onOkClick(Dialog dialog);
    }

    public interface OnCancelClickListener {
        void onCancelClick(Dialog dialog);
    }

}
