package ua.at.tsvetkov.netchecker;

/**
 * Created by Alexandr Tsvetkov on 11.10.2015.
 */

public abstract class NetStatusCallback {

    public NetStatusCallback() {

    }

    public abstract void onResume(NetStatus status);

}
