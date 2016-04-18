package com.roselism.spot.model.dao.operator;

import com.roselism.spot.model.dao.listener.OnDeleteListener;
import com.roselism.spot.model.domain.hyper.Deleteable;

/**
 * Created by simon on 16-4-18.
 */
public interface DeleteOperater {

    void delete(Deleteable deleteable, OnDeleteListener<Deleteable> listener);
}