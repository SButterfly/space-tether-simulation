package com.sbutterfly.core.callbackTether;

import com.sbutterfly.engine.trace.Axis;

/**
 * @author s-ermakov
 */
@SuppressWarnings({"checkstyle:membername", "checkstyle:methodname", "checkstyle:parametername"})
class CallbackTetherTraceService {

    private static final Axis EMPTY_AXIS = new Axis("EMPTY", "");

    private static final Axis M_1_AXIS = new Axis("m1", "m1, кг");
    private static final Axis M_2_AXIS = new Axis("m2", "m2, кг");
    private static final Axis M_3_AXIS = new Axis("m3", "m3, кг");

    private static final Axis KL_AXIS = new Axis("KL", "KL");
    private static final Axis KV_AXIS = new Axis("KV", "KV");

    private static final Axis A_AXIS = new Axis("a", "a");
    private static final Axis B_AXIS = new Axis("b", "b");

    private static final Axis RO_AXIS = new Axis("ρ", "ρ, кг/м3");

    private static final Axis LK_AXIS = new Axis("Lk", "Lk, м");
    private static final Axis H_AXIS = new Axis("H", "H, км");

    private static final Axis L_AXIS = new Axis("L", "L, м");
    private static final Axis V_AXIS = new Axis("V", "V, м/с");
    private static final Axis L_P_AXIS = new Axis("Lp", "Lp, м");
    private static final Axis V_P_AXIS = new Axis("Vp", "Vp, м/с");

    private static final Axis TETTA_AXIS = new Axis("θ", "θ, рад");
    private static final Axis TETTA_T_AXIS = new Axis("θ˙", "θ˙, рад/с");

    private static final Axis TETTA_DEGRES_AXIS = new Axis("θградусы", "θ, градусы");
    private static final Axis TETTA_T_DEGRES_AXIS = new Axis("θградусы/сек", "θ, градусы/сек");
    private static final Axis BETTA_AXIS = new Axis("β", "β, рад");
    private static final Axis BETTA_T_AXIS = new Axis("β˙", "β˙, рад/с");

    private static final Axis TIME_AXIS = new Axis("t", "t, c");
    private static final Axis X_AXIS = new Axis("x", "x, м");
    private static final Axis Y_AXIS = new Axis("y", "y, м");
    private static final Axis T_AXIS = new Axis("T", "T, Н");

    private static final Axis X1_AXIS = new Axis("x1", "x1, м");
    private static final Axis Y1_AXIS = new Axis("y1", "y1, м");
    private static final Axis VX1_AXIS = new Axis("vx1", "vx1, м/с");
    private static final Axis VY1_AXIS = new Axis("vy1", "vy1, м/с");

    private static final Axis X2_AXIS = new Axis("x2", "x2, м");
    private static final Axis Y2_AXIS = new Axis("y2", "y2, м");
    private static final Axis VX2_AXIS = new Axis("vx2", "vx2, м/с");
    private static final Axis VY2_AXIS = new Axis("vy2", "vy2, м/с");

    private static final Axis L_TP_AXIS = new Axis("tp", "Переходный процесс по длине, м");

    private static final Axis SPEED_TP_AXIS = new Axis("speed",
            "Переходный процесс по скорости, м/с");

    private static final Axis TETHER_DEFORMATION_AXIS = new Axis("td", "Деформация троса");

    private static final Axis POINT_LENGTH_AXIS = new Axis("point length",
            "Расстояние между КА, м");

    private static final Axis TETHER_ELONGATION_AXIS = new Axis("tether alongation",
            "Удлинение троса, м");

    private static final Axis F_POWER_AXIS = new Axis("F", "F, Н");

    private static final Axis FP_POWER_AXIS = new Axis("fp", "F номинальная, Н");

    private static final Axis POWER_TP_AXIS = new Axis("pp", "Переходный процесс по силе, H");

    private CallbackTetherTraceService() {
    }

    public static Axis empty() {
        return EMPTY_AXIS;
    }

    public static Axis M1_axis() {
        return M_1_AXIS;
    }

    public static Axis M2_axis() {
        return M_2_AXIS;
    }

    public static Axis M3_axis() {
        return M_3_AXIS;
    }

    public static Axis KL_axis() {
        return KL_AXIS;
    }

    public static Axis KV_axis() {
        return KV_AXIS;
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

    public static Axis Lp_axis() {
        return L_P_AXIS;
    }

    public static Axis V_p_axis() {
        return V_P_AXIS;
    }

    public static Axis Tetta_degress_axis() {
        return TETTA_DEGRES_AXIS;
    }

    public static Axis Tetta_t_degress_axis() {
        return TETTA_T_DEGRES_AXIS;
    }

    public static Axis Tetta_p_axis() {
        return TETTA_AXIS;
    }

    public static Axis Tettat_p_axis() {
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

    public static Axis X1_axis() {
        return X1_AXIS;
    }

    public static Axis Y1_axis() {
        return Y1_AXIS;
    }

    public static Axis VX1_axis() {
        return VX1_AXIS;
    }

    public static Axis VY1_axis() {
        return VY1_AXIS;
    }

    public static Axis X2_axis() {
        return X2_AXIS;
    }

    public static Axis Y2_axis() {
        return Y2_AXIS;
    }

    public static Axis VX2_axis() {
        return VX2_AXIS;
    }

    public static Axis VY2_axis() {
        return VY2_AXIS;
    }

    public static Axis Line_transition_process_axis() {
        return L_TP_AXIS;
    }

    public static Axis Speed_transition_process_axis() {
        return SPEED_TP_AXIS;
    }

    public static Axis Power_transition_process_axis() {
        return POWER_TP_AXIS;
    }

    public static Axis Tether_deformation_axis() {
        return TETHER_DEFORMATION_AXIS;
    }

    public static Axis Tether_elongation_axis() {
        return TETHER_ELONGATION_AXIS;
    }

    public static Axis Point_length_axis() {
        return POINT_LENGTH_AXIS;
    }

    public static Axis F_power_axis() {
        return F_POWER_AXIS;
    }

    public static Axis Fp_power_axis() {
        return FP_POWER_AXIS;
    }
}
