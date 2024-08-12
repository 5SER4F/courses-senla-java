package org.uhanov;

import org.uhanov.beans.BeanFactory;
import org.uhanov.conroller.Controller;

public class Main {

    public static void main(String[] args) throws Exception {
        BeanFactory bF = BeanFactory.getInstance();
        Controller controller = (Controller) bF.getBeanByCanonicalName(Controller.class.getCanonicalName());
        System.out.println(controller.execute());;
    }
}