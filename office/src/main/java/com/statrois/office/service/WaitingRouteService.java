package com.statrois.office.service;

import com.statrois.common.bean.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class WaitingRouteService {

    private final Lock lock = new ReentrantLock(true);
    private final List<Route> list = new ArrayList<>();

    public List<Route> list() {
        return list;
    }

    public void add(Route route) {
        try {
            lock.lock();
            list.add(route);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Route route) {
        try {
            lock.lock();
            list.removeIf(route::equals);
        } finally {
            lock.unlock();
        }
    }



}
