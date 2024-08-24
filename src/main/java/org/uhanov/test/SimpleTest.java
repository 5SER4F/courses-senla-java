package org.uhanov.test;

import org.uhanov.conroller.CRUDController;
import org.uhanov.model.EntityWithUUID;

import java.util.function.Function;

public class SimpleTest<Model extends EntityWithUUID, DTO> {
    private final CRUDController<DTO> controller;
    private final Function<Model, DTO> toDto;
    private final Model entity1;
    private final Model entity2;
    private final DTO entityPatch;

    public SimpleTest(CRUDController<DTO> controller, Function<Model,
            DTO> toDto, Model entity1, Model entity2, DTO entityPatch) {
        this.controller = controller;
        this.toDto = toDto;
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entityPatch = entityPatch;
    }

    public void doTest() {
        System.out.println();
        System.out.println("Start test " + controller.getClass().getName());

        System.out.println("Add first " + controller.add(toDto.apply(entity1)));
        System.out.println("Add second " + controller.add(toDto.apply(entity2)));

        System.out.println("Read first " + controller.get(entity1.getId()));

        System.out.println("Delete Second " + controller.delete(entity2.getId()));


        controller.update(entityPatch);

        System.out.println("Update First" + controller.get(entity1.getId()));

        System.out.println();
    }
}
