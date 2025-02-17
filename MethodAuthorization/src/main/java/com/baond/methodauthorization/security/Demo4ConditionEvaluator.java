package com.baond.methodauthorization.security;

import org.springframework.stereotype.Component;

@Component
public class Demo4ConditionEvaluator {

    public boolean condition(String smth){
        System.out.println(smth);
        return true; // the complex authorization condition.
    }
}
