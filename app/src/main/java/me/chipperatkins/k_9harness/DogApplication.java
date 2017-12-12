package me.chipperatkins.k_9harness;

import android.app.Application;

/**
 * Created by user on 12/8/2017.
 */

public class DogApplication extends Application {

    private Dog activeDog = null;
    private boolean btToggle = false;

    public Dog getActiveDog() {
        return activeDog;
    }

    public void setActiveDog(Dog active){
        activeDog = active;
    }

    public boolean btConnected() {
        return btToggle;
    }

    public void btToggle(){
        if (btToggle) {
            btToggle = false;
        }
        else {
            btToggle = true;
        }
    }
}
