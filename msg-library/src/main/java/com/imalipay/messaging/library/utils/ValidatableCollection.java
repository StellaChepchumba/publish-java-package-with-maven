package com.imalipay.messaging.library.utils;

import java.util.Collection;

import javax.validation.Valid;

import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class ValidatableCollection<E> implements Collection<E> 
{
    @Valid
    @Delegate
    private Collection<E> collection;
}