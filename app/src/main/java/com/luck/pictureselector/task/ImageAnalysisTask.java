package com.luck.pictureselector.task;

import android.os.AsyncTask;



/**
 * Created by hasee on 2019-03-31.
 */
public class ImageAnalysisTask extends AsyncTask<String, Integer, Integer> {

    public  static  final int TYPE_SUCCESS =0;//成功
    public  static  final int TYPE_FAILED =1;//失败
    public  static  final int TYPE_PAUSED =2;//暂停
    public  static  final int TYPE_CANCELED =3;//取消
    private ImageAnalysisListener listener;
    private boolean isCanceled =false;
    private boolean isPaused=false;
    private int lastProgress;

    public ImageAnalysisTask(ImageAnalysisListener listener) {
        this.listener = listener;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Integer doInBackground(String... params) {

        try {

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }




    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case  TYPE_FAILED:
                listener.onFaied();
                break;
            case  TYPE_PAUSED:
                listener.onPaused();
                break;
            case  TYPE_CANCELED:
                listener.onCancled();
                break;
            default:
                    break;
        }
    }
   public void pasuseDownLload(){
        isPaused=true;
   }
   public void cancelDownload(){
        isCanceled=true;
   }
    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        if(progress>lastProgress){
            listener.onProgress(progress);
            lastProgress=progress;
        }
    }

}





