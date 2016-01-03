package com.jfsiot.touchselect.touchselecttest.helper;

import android.widget.EditText;

import java.util.List;

/**
 * Created by SSS on 2016-01-03.
 */
public class TextOffsetHelper {

    public static int getOffsetTextList(List<Integer> indexList, int offset, boolean isLeft, int endIndex){
//        int value = 0;
//        if(offset < indexList.get(0))
//            return 0;
//        for(int i : indexList){
//            if(i > offset){
//                value = i;
//                break;
//            }
//            value = i;
//        }
        for(int i = 0; i < indexList.size(); i++){
            if(indexList.get(i) > offset){
                if(indexList.get(i) > endIndex -1)
                    return endIndex -1;
                else if(isLeft && i > 0)
                    return indexList.get(i-1);
                else if(i == 0)
                    return 0;
                else return indexList.get(i);
            }
        }
//        if(value > editText.getText().length() - 1)
//            value = editText.getText().length() - 1;
        return 0;
    }


    /**
     * @param indexList Word index List of text List
     * @param position Text selection start index of EditText
     */
    public static int jumpLeftSide(List<Integer> indexList, int position){
        int value = 0;
        for(int i : indexList){
            if(i >= position){
                break;
            }else{
                value = i;
            }
        }
        return value;
    }

    /**
     * @param indexList Word index List of text List
     * @param position Text selection end index of EditText.(EditText.getSlectionEnd())
     */
    public static int jumpRightSide(List<Integer> indexList, int position){
        int value = 0;
        for(int i : indexList){
            if(i > position){
                value = i;
                break;
            }
        }
        return value;
    }

    /**
     * @param editText : Current usage
     * */
    public static void getPositionXY(int location[], EditText editText, int offset){
//        int[] screenLocation = new int[2];
//        editText.getLocationOnScreen(screenLocation);
        int line = editText.getLayout().getLineForOffset(offset);
        int top = editText.getLayout().getLineBottom(line);
        int bottom = editText.getLayout().getLineTop(line);
        int x = ((int) editText.getLayout().getPrimaryHorizontal(offset));
        int y = (bottom - top)/2 + top;
        location[0] = x;
        location[1] = y;
    }

    public static void getPositionLineOffset(int location[], EditText editText, int x, int y){
        int line = editText.getLayout().getLineForVertical(y);
        int offset = editText.getLayout().getOffsetForHorizontal(line, x);
        location[0] = line;
        location[1] = offset;
    }
}
