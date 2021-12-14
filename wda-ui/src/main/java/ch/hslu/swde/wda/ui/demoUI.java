package ch.hslu.swde.wda.ui;

import ch.hslu.swde.wda.domain.User;

import java.util.List;

public class demoUI {

    public static void main(String[] args) {
       UI ui =  new UI();
       List<User> userList = ui.selectAllUserData();

      // ui.startFromBeginning();
    }
}
