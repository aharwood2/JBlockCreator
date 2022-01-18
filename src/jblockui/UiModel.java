package jblockui;

import jblockenums.EInputType;

public class UiModel
{
    private static UiModel instance;

    private UiModel(){}

    public static UiModel getInstance(){
        if(instance == null){
            synchronized (UiModel.class) {
                if(instance == null){
                    instance = new UiModel();
                }
            }
        }
        return instance;
    }
}
