package com.bee.wschat.dao;

import com.bee.wschat.entity.Participant;

import java.util.List;

/**
 * Created by Chaklader on 7/24/17.
 */
public interface ParticipantDao {
    List<Participant> getParticipants(List<String> names);
}
