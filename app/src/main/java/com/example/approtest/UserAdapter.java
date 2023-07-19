package com.example.approtest;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter {

    class UserViewHolder extends RecyclerView.viewHolder {
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding)
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
