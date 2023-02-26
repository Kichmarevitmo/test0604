package Lab1.Models;

import java.time.LocalDateTime;

public interface IObserver {
    void update(LocalDateTime timeStamp) throws Exception;
}

