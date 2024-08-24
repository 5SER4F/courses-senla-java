package org.uhanov.conroller;

import java.util.UUID;

public interface CRUDController<T> {

    Object add(T dto);


    Object update(T dto);

    Object delete(UUID uuid);

    Object get(UUID uuid);
}
