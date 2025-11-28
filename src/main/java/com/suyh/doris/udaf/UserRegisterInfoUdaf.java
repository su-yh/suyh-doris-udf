package com.suyh.doris.udaf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author suyh
 * @since 2025-11-28
 */
public class UserRegisterInfoUdaf {
    public static class State {
        /*some variables if you need */
        private long minCtime = Long.MAX_VALUE;
        private Long id;
        private String sourceTb;
        private String gaid;

        public void reset() {
            this.minCtime = Long.MAX_VALUE;;
            this.id = null;
            this.sourceTb = null;
            this.gaid = null;
        }

        public void merge(State rhs) {
            this.minCtime = rhs.minCtime;;
            this.id = rhs.id;
            this.sourceTb = rhs.sourceTb;
            this.gaid = rhs.gaid;
        }
    }

    /*required*/
    public UserRegisterInfoUdaf.State create() {
        /* here could do some init work if needed */
        return new UserRegisterInfoUdaf.State();
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
    public void add(State state, String sourceTb, Long id, Long ctime, String gaid) throws Exception {
        /* here doing update work when input data*/
        // if (val != null) {
        //     state.sum += val;
        // }
        if (ctime == null || ctime <= 0L) {
            return;
        }

        if (state.minCtime > ctime) {
            state.minCtime = ctime;
            state.sourceTb = sourceTb;
            state.id = id;
            state.gaid = gaid;
        }
    }

    /**
     * merge 方法是对不同be 节点中的数据进行合并，得到最后一个结果
     */
    /*required*/
    public void merge(State state, State rhs) throws Exception {
        /* merge data from state */
        if (state.minCtime > rhs.minCtime) {
            state.merge(rhs);
        }
    }

    /*required*/
    public void serialize(State state, DataOutputStream out) throws IOException {
        /* serialize some data into buffer */
        out.writeLong(state.minCtime);
        out.writeUTF(state.sourceTb);
        out.writeLong(state.id);
        out.writeUTF(state.gaid);
    }

    /*required*/
    public void deserialize(State state, DataInputStream in) throws IOException {
        /* deserialize get data from buffer before you put */
        state.minCtime = in.readLong();
        state.sourceTb = in.readUTF();
        state.id = in.readLong();
        state.gaid = in.readUTF();
    }

    public String getValue(State state) throws Exception {
        /* return finally result */
        return state.sourceTb + " &&@@ " + state.id + " &&@@ " + state.gaid + " &&@@ " + state.minCtime;
    }
}
