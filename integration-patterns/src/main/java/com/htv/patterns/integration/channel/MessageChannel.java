package com.htv.patterns.integration.channel;
import com.htv.patterns.integration.core.Message;
public interface MessageChannel<T> { void send(Message<T> message); }
