package com.example.mathschool.interfaces;

import java.io.IOException;

public interface OnAdapterItemClicked {

    void onItemClicked(int position,String subject) throws IOException;
}
