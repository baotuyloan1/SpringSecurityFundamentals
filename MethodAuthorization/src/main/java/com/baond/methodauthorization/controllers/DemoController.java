package com.baond.methodauthorization.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    // @PreAuthorize

    @GetMapping("/demo1")
    @PreAuthorize("hasAuthority('read')") // hasAuthority() hasAnyAuthority() hasRole() hasAnyRole()
    public String demo1() {
        return "Demo1";
    }

    @GetMapping("/demo2")
    @PreAuthorize("hasAnyAuthority('write', 'read')")
    public String demo2() {
        return "Demo2";
    }

    @GetMapping("/demo3/{smth}")
//    @PreAuthorize("#something == authentication.name") // authentication from SecurityContext
//    @PreAuthorize("(#something == authentication.principal.username) or hasAnyAuthority('read', 'write')")
    @PreAuthorize(
            """
                                (#something == authentication.principal.username) or
                                hasAnyAuthority('read', 'write')
                    """
    )
    public String demo3(@PathVariable("smth") String something) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Demo3";
    }


    @GetMapping("/demo4/{smth}")
    @PreAuthorize("@demo4ConditionEvaluator.condition(#something)")
    public String demo4(@PathVariable("smth") String something) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Demo 4";
    }


    // @PostAuthorize
    @GetMapping("/demo5")
    @PostAuthorize("returnObject != 'Demo 5'") // The rules will be applied after the execution of the method. Only restrict the return value.
    public String demo5() {
        System.out.println("Executed !"); // never use @PostAuthorize with methods that change data
        return "Demo 5";
    }

    // @PreFilter => works with either array or Collection
    @GetMapping("/demo6")
    @PreFilter("filterObject.contains('a')") // Filter the input values which satisfy the filter.
    public String demo6(@RequestBody List<String> values) {
        System.out.println("Values: "+ values); // never use @PostAuthorize with methods that change data
        return "Demo 6";
    }

    // @PostFilter => the returned type mt be either a Collection or an array.
    @GetMapping("/demo7")
    @PostFilter("filterObject.contains('a')") // Filter the input values which satisfy the filter.
    public List<String> demo7() {
        return List.of("a", "b", "c", "ab", "db");
    }




}
