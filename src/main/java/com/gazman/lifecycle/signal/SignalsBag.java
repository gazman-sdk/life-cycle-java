package com.gazman.lifecycle.signal;

import com.gazman.lifecycle.log.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Created by Ilya Gazman on 2/24/2015.
 */
public final class SignalsBag {

    static HashMap<Class<?>, Signal> map = new HashMap<>();

    private SignalsBag() {
    }

    /**
     * Will get you a signal from the given interface type, there will be only one instance of it in the system
     *
     * @param type the signal type
     * @return Signal from given type
     */
    public static <T> Signal<T> inject(Class<T> type) {
        @SuppressWarnings("unchecked")
        Signal<T> signal = map.get(type);
        if (signal == null) {
            signal = new Signal<T>(type);
            map.put(type, signal);
        }
        return signal;
    }

    /**
     * Create new signal from given type
     *
     * @param type the interface type
     * @return Signal from given type
     */
    public static <T> Signal<T> create(Class<T> type) {
        //noinspection unchecked
        return new Signal(type);
    }

    public static <T> T log(Class<T> tClass, final String tag) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new InvocationHandler() {

            private Logger logger = Logger.create(tag);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StringBuilder stringBuilder = new StringBuilder();
                if (args != null) {
                    for (Object arg : args) {
                        stringBuilder.append(arg).append(" ");
                    }
                }
                logger.d(method.getName(), stringBuilder);
                return null;
            }
        });
    }

}