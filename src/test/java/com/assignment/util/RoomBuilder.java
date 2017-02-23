package com.assignment.util;

import java.time.LocalTime;

import com.assignment.domain.Room;

public final class RoomBuilder {
    private Long id;
    private String name;
    private String code;
    private LocalTime startTime;
    private LocalTime endTime;

    private RoomBuilder() {
    }

    public static RoomBuilder aRoom() {
        return new RoomBuilder();
    }

    public static RoomBuilder aRoomWithDefaults() {
        return new RoomBuilder().withCode("123d")
                .withName("Vegas")
                .withStartTime(ReservationUtils.beginningOfTheWorkDay().toLocalTime())
                .withEndTime(ReservationUtils.endOfTheWorkDay().toLocalTime());
    }

    public RoomBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RoomBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RoomBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public RoomBuilder withStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public RoomBuilder withEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Room build() {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCode(code);
        room.setStartTime(startTime);
        room.setEndTime(endTime);
        return room;
    }
}
