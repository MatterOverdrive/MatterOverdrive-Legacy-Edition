/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */
package matteroverdrive.animation;

import matteroverdrive.util.math.MOMathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/31/2015.
 */
public class AnimationTimeline<T extends AnimationSegment> {
    protected int time;
    private boolean loopable;
    private int duration;
    private List<T> segments;
    private int lastSegmentBegin;

    public AnimationTimeline(boolean loopable, int duration) {
        segments = new ArrayList<>();
        this.loopable = loopable;
        this.duration = duration;
    }

    public double getPercent() {
        return (double) time / (double) duration;
    }

    public void addSegment(T segment) {
        segments.add(segment);
    }

    public void addSegmentSequential(T segment) {
        segment.begin = lastSegmentBegin;
        lastSegmentBegin += segment.length;
        segments.add(segment);
    }

    public T getCurrentSegment() {
        for (T segment : segments) {
            if (MOMathHelper.animationInRange(time, segment.begin, segment.length)) {
                return segment;
            }
        }
        return null;
    }

    public void tick() {
        if (time < duration) {
            time++;
        } else if (loopable) {
            time = 0;
        }
    }

    public void setTime(int time) {
        this.time = time;
    }
}
