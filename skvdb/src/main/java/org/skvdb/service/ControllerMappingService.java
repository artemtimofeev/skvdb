package org.skvdb.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ControllerMappingService extends ConcurrentHashMap<String, String> {
}
