package com.example.doma.travel_diary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Created by seungyunk on 2017-02-19.
 */

public class imageManager {
    public void Bitmapsetter(Context context, int addressOfImageView) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //이미지를 decoding 할 때 이미지의 크기만을 먼저 불러와 OOM을 불러 일으킬 큰 이미지를 불러오더라도 선처리를 가능하도록 해줌
        BitmapFactory.decodeResource(context.getResources(), addressOfImageView, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
///inJustDecodeBounds 설정.. 여러 디코딩 메소드중 작은 뷰에 크기를 알아내는 영역

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
//    1번의 과정을 통해 이미지의 크기를 알게되었다면 이제 적절한 크기로 이미지를 리사이징할 차례이다.
//    inSampleSize 설정을 통해 이미지를 작은 사이즈로 리사이즈 할 수 있다.
//    inSampleSize는 픽셀수 기준으로 1/inSampleSize 크기로 이미지를 줄여준다. (가로,세로 기준)
//
//    리사이징을 위한 decoder가 inSampleSize를 2의 배수에 가까운 수로 버림하여 계산하기 때문에 2의 배수로 값을 계산하도록 하고, 선처리된 높이와 폭을 바탕으로 이미지를 표시할 뷰의 크기보다 작지 않은 크기로 가장 큰 inSampleSize값을 산출한다.

    public static Bitmap decodeSampleBitmapFromURI(Context context, Uri uri, int reqWidth, int reqHeight) {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bt = null;
        try {
            bt = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (Exception e) {
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return bt;
    }


}
