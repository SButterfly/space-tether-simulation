package com.sbutterfly.core.callbackrope;

import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.trace.Axis;

import java.util.Arrays;
import java.util.List;

/**
 * @author s-ermakov
 */
@SuppressWarnings({"checkstyle:membername", "checkstyle:methodname", "checkstyle:parametername"})
class RopeTraceService {

    private static final Axis M_1_AXIS = new Axis("m1", "m1, кг");
    private static final Axis M_2_AXIS = new Axis("m2", "m2, кг");
    private static final Axis RO_AXIS = new Axis("ρ", "ρ, кг/м3");
    private static final Axis A_AXIS = new Axis("a", "a");
    private static final Axis B_AXIS = new Axis("b", "b");
    private static final Axis LK_AXIS = new Axis("Lk", "Lk, м");
    private static final Axis H_AXIS = new Axis("H", "H, км");

    private static final Axis L_AXIS = new Axis("L", "L, м");
    private static final Axis V_AXIS = new Axis("V", "V, м/с");
    private static final Axis TETTA_AXIS = new Axis("θ", "θ, рад");
    private static final Axis TETTA_T_AXIS = new Axis("θ˙", "θ˙, рад/с");
    private static final Axis BETTA_AXIS = new Axis("β", "β, рад");
    private static final Axis BETTA_T_AXIS = new Axis("β˙", "β˙, рад/с");

    private static final Axis TIME_AXIS = new Axis("t", "t, c");
    private static final Axis X_AXIS = new Axis("x", "x, м");
    private static final Axis Y_AXIS = new Axis("y", "y, м");
    private static final Axis T_AXIS = new Axis("T", "T, Н");

    private static final GroupAxisDescription ROPE_GROUP = new GroupAxisDescription("Параметры тросовой системы",
            Arrays.asList(M_1_AXIS, M_2_AXIS, RO_AXIS, LK_AXIS, H_AXIS));

    private static final GroupAxisDescription SYSTEM_GROUP = new GroupAxisDescription("Параметры закона",
            Arrays.asList(A_AXIS, B_AXIS));

    private static final GroupAxisDescription START_PARAMS_GROUP = new GroupAxisDescription("Начальные параметры",
            Arrays.asList(L_AXIS, V_AXIS, TETTA_AXIS, BETTA_AXIS));

    private static final List<GroupAxisDescription> GROUP_AXIS_DESCRIPTIONS = Arrays.asList(
            ROPE_GROUP, SYSTEM_GROUP, START_PARAMS_GROUP);

    private RopeTraceService() {
    }

    public static Axis M1_axis() {
        return M_1_AXIS;
    }

    public static Axis M2_axis() {
        return M_2_AXIS;
    }

    public static Axis Ro_axis() {
        return RO_AXIS;
    }

    public static Axis A_axis() {
        return A_AXIS;
    }

    public static Axis B_axis() {
        return B_AXIS;
    }

    public static Axis Lk_axis() {
        return LK_AXIS;
    }

    public static Axis H_axis() {
        return H_AXIS;
    }

    public static Axis L_axis() {
        return L_AXIS;
    }

    public static Axis V_axis() {
        return V_AXIS;
    }

    public static Axis Tetta_axis() {
        return TETTA_AXIS;
    }

    public static Axis Tetta_t_axis() {
        return TETTA_T_AXIS;
    }

    public static Axis Betta_axis() {
        return BETTA_AXIS;
    }

    public static Axis Betta_t_axis() {
        return BETTA_T_AXIS;
    }

    public static Axis Time_axis() {
        return TIME_AXIS;
    }

    public static Axis X_axis() {
        return X_AXIS;
    }

    public static Axis Y_axis() {
        return Y_AXIS;
    }

    public static Axis T_axis() {
        return T_AXIS;
    }

    public static List<GroupAxisDescription> getGroupAxisDescriptions() {
        return GROUP_AXIS_DESCRIPTIONS;
    }

    public static List<Axis> getSupportedAxis() {
        return Arrays.asList(M_1_AXIS, M_2_AXIS, RO_AXIS, A_AXIS, B_AXIS, LK_AXIS, H_AXIS, L_AXIS, H_AXIS,
                L_AXIS, V_AXIS, TETTA_AXIS, TETTA_T_AXIS, BETTA_AXIS, BETTA_T_AXIS,
                TIME_AXIS, X_AXIS, Y_AXIS, T_AXIS);
    }
}
