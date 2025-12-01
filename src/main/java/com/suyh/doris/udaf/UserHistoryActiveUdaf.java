package com.suyh.doris.udaf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author suyh
 * @since 2025-11-28
 */
public class UserHistoryActiveUdaf {
    public static class State implements Serializable {
        private static final long serialVersionUID = 2595606523789532805L;

        /*some variables if you need */
        public long maxHistoryActiveCtime = 0;
        public Long ctime = null;
        public String channel = null;

        public void reset() {
            this.maxHistoryActiveCtime = 0L;
            this.ctime = null;
            this.channel = null;
        }

        public void merge(State rhs) {
            this.maxHistoryActiveCtime = Math.max(this.maxHistoryActiveCtime, rhs.maxHistoryActiveCtime);
            // channel 不需要修改
        }
    }

    /*required*/
    public State create() {
        /* here could do some init work if needed */
        return new State();
    }

    /*required*/
    public void destroy(State state) {
        /* here could do some destroy work if needed */
    }

    /*Not Required*/
    public void reset(State state) {
        /*if you want this udaf function can work with window function.*/
        /*Must impl this, it will be reset to init state after calculate every window frame*/
        state.reset();
    }

    /**
     * add 方法是在一个be 节点中，对每条数据进行依次调用。
     */
    /*required*/
    //first argument is State, then other types your input
    public void add(
            State state,
            Long ctime, String channel,
            Long regTs, String regChannel,
            Long loginTs, String loginChannel,
            Long rechargeTs, String rechargeChannel,
            Long withdrawalTs, String withdrawalChannel) throws Exception {
        /* here doing update work when input data*/
        // if (val != null) {
        //     state.sum += val;
        // }
        if (ctime == null || ctime <= 0L) {
            return;
        }
        if (state.ctime == null) {
            state.ctime = ctime;
        }
        if (state.channel == null) {
            state.channel = channel;
        }
        if (regChannel == null) {
            regChannel = "";
        }
        if (loginChannel == null) {
            loginChannel = "";
        }
        if (rechargeChannel == null) {
            rechargeChannel = "";
        }
        if (withdrawalChannel == null) {
            withdrawalChannel = "";
        }

        if (regTs != null && regTs < state.ctime && !regChannel.equalsIgnoreCase(state.channel)) {
            state.maxHistoryActiveCtime = Math.max(state.maxHistoryActiveCtime, regTs);
        }
        if (loginTs != null && loginTs < state.ctime && !loginChannel.equalsIgnoreCase(state.channel)) {
            state.maxHistoryActiveCtime = Math.max(state.maxHistoryActiveCtime, loginTs);
        }
        if (rechargeTs != null && rechargeTs < state.ctime && !rechargeChannel.equalsIgnoreCase(state.channel)) {
            state.maxHistoryActiveCtime = Math.max(state.maxHistoryActiveCtime, rechargeTs);
        }
        if (withdrawalTs != null && withdrawalTs < state.ctime && !withdrawalChannel.equalsIgnoreCase(state.channel)) {
            state.maxHistoryActiveCtime = Math.max(state.maxHistoryActiveCtime, withdrawalTs);
        }
    }

    /**
     * merge 方法是对不同be 节点中的数据进行合并，得到最后一个结果
     */
    /*required*/
    public void merge(State state, State rhs) throws Exception {
        /* merge data from state */
        state.merge(rhs);
    }

    /*required*/
    public void serialize(State state, DataOutputStream out) throws IOException {
        /* serialize some data into buffer */
        out.writeLong(state.maxHistoryActiveCtime);
        out.writeLong(state.ctime);
        out.writeUTF(state.channel);
    }

    /*required*/
    public void deserialize(State state, DataInputStream in) throws IOException {
        /* deserialize get data from buffer before you put */
        state.maxHistoryActiveCtime = in.readLong();
        state.ctime = in.readLong();
        state.channel = in.readUTF();
    }

    public Long getValue(State state) throws Exception {
        /* return finally result */

        return state.maxHistoryActiveCtime == 0 ? null : state.maxHistoryActiveCtime;
    }
}
