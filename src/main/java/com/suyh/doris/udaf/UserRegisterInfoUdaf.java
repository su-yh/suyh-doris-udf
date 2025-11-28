package com.suyh.doris.udaf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author suyh
 * @since 2025-11-28
 */
public class UserRegisterInfoUdaf {
    public static class State implements Serializable {
        private static final long serialVersionUID = 2595606523789532805L;

        /*some variables if you need */
        public String sourceTb = "";
        public Long id = 0L;
        public String uid = "";
        public String channel = "";
        public long minCtime = Long.MAX_VALUE;
        public String gaid = "";
        public String pn = "";
        public Integer day = 0;

        public void reset() {
            this.sourceTb = "";
            this.id = 0L;
            this.uid = "";
            this.channel = "";
            this.minCtime = Long.MAX_VALUE;;
            this.gaid = "";
            this.pn = "";
            this.day = 0;
        }

        public void merge(State rhs) {
            this.sourceTb = rhs.sourceTb;
            this.id = rhs.id;
            this.uid = rhs.uid;
            this.channel = rhs.channel;
            this.minCtime = rhs.minCtime;;
            this.gaid = rhs.gaid;
            this.pn = rhs.pn;
            this.day = rhs.day;
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
            String sourceTb, Long id, String uid, String channel, Long ctime, String gaid, String pn, Integer day) throws Exception {
        /* here doing update work when input data*/
        // if (val != null) {
        //     state.sum += val;
        // }
        if (ctime == null || ctime <= 0L) {
            return;
        }

        if (state.minCtime > ctime) {
            state.sourceTb = sourceTb == null ? "" : sourceTb;
            state.id = id == null ? 0L : id;
            state.uid = uid == null ? "" : uid;
            state.channel = channel == null ? "" : channel;
            state.minCtime = ctime;
            state.gaid = gaid == null ? "" : gaid;
            state.pn = pn == null || pn.isEmpty() ? "hy" : pn;
            state.day = day == null ? 0 : day;
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
        out.writeUTF(state.sourceTb);
        out.writeLong(state.id);
        out.writeUTF(state.uid);
        out.writeUTF(state.channel);
        out.writeLong(state.minCtime);
        out.writeUTF(state.gaid);
        out.writeUTF(state.pn);
        out.writeInt(state.day);
    }

    /*required*/
    public void deserialize(State state, DataInputStream in) throws IOException {
        /* deserialize get data from buffer before you put */
        state.sourceTb = in.readUTF();
        state.id = in.readLong();
        state.uid = in.readUTF();
        state.channel = in.readUTF();
        state.minCtime = in.readLong();
        state.gaid = in.readUTF();
        state.pn = in.readUTF();
        state.day = in.readInt();
    }

    public ArrayList<Object> getValue(State state) throws Exception {
        /* return finally result */
        // return state.sourceTb + " &&@@ " + state.id + " &&@@ " + state.gaid + " &&@@ " + state.minCtime;

        ArrayList<Object> arrays = new ArrayList<>();
        arrays.add(state.sourceTb);
        arrays.add(state.id);
        arrays.add(state.uid);
        arrays.add(state.channel);
        arrays.add(state.minCtime);
        arrays.add(state.gaid);
        arrays.add(state.pn);
        arrays.add(state.day);
        return arrays;
    }
}
