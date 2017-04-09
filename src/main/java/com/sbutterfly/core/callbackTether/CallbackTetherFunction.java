package com.sbutterfly.core.callbackTether;

import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.utils.Func;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Функция моделирования развертки КА.
 * <p>
 * Created by Sergei on 26.02.2015
 */
@SuppressWarnings({"checkstyle:all"})
public class CallbackTetherFunction implements Function {

    // массы большого и малого КА
    private final double m1;
    private final double m2;
    
    // коэфициент инертности механизма
    // измеряется в кг
    private final double m3;

    // коэфициенты обратной связи
    private final double KL;
    private final double KV;

    // параметры развертывания
    private final double a;
    private final double b;

    // конечная длина троса
    private final double Lk;

    // МАТЕРИАЛ ТРОСА
    // Модуль Юнга н/м2
    private final double Eung = 2.5 * 10_000_000_000L;

    // Диаметр троса (м)
    private final double Dt = 0.0006;
    private final double rr = Dt / 2;
    private final double SS = PI * rr * rr;
    private final double c = Eung * SS;

    // Гравитационный параметры Земли (м)
    private final double K = 398600 * cub(1000);

    // Высота орбиты (м)
    private final double H = 3 * 100_000;

    // Средний радиус Земли (м)
    private final double R3 = 6371.02 * 1000;

    // Радиус орбиты
    private final double Rop = R3 + H;

    // СКОРОСТИ
    // линейная скорость по орбите (м/с)
    private final double Vop = sqrt(K / Rop);

    // угловая скорость
    private final double Om = Vop / Rop;

    // КООРДИНАТЫ
    // центр системы
    private final double xc = Rop;
    private final double yc = 0.0;

    private final double rc = length(xc, yc);

    // ПРОЕКЦИИ СКОРОСТЕЙ
    private final double Vxc = 0.0;
    private final double Vyc = Vop;

    // Минимальная сила Fcmin
    private final double Fcmin = 0.01;

    // Впомогательные величины

    private final double Om2 = Om*Om;

    private final double _3Lk;

    // m1 + m2
    // -------
    //  m1*m2
    private final double M1plusM2;

    //  m1*m2
    // -------
    // m1 + m2
    private final double M1multM2;

    // (-m2/(m1 + m2))
    private final double minusM2;

    // (m1/(m1 + m2))
    private final double plusM1;

    // m1 + m2
    // ------- * Om*Om
    //  m1*m2
    private final double M1multM2_Om2;

    public CallbackTetherFunction(double m1, double m2, double m3,
                                  double KL, double KV,
                                  double a, double b,
                                  double Lk) {
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.KL = KL;
        this.KV = KV;
        this.a = a;
        this.b = b;
        this.Lk = Lk;
        this.M1plusM2 = (m1 + m2)/(m1*m2);
        this.M1multM2 = (m1*m2)/(m1+m2);
        this.minusM2 = (-m2)/(m1+m2);
        this.plusM1 = (m1)/(m1+m2);
        this.M1multM2_Om2 = M1multM2*Om2;
        this._3Lk = 3 * Lk;
    }

    /**
     *  -m2
     * ------- * L * cos(tetta) + rc
     * m1 + m2
     */
    public double getX1(double L, double tetta) {
        return minusM2 * L * cos(tetta) + rc;
    }

    /**
     *    m1
     * ------- * L * cos(tetta) + rc
     * m1 + m2
     */
    public double getX2(double L, double tetta) {
        return plusM1 * L * cos(tetta) + rc;
    }

    /**
     *  -m2
     * ------- * L * sin(tetta) + yc
     * m1 + m2
     */
    public double getY1(double L, double tetta) {
        return minusM2 * L * sin(tetta) + yc;
    }

    /**
     *    m1
     * ------- * L * sin(tetta) + yc
     * m1 + m2
     */
    public double getY2(double L, double tetta) {
        return plusM1 * L * sin(tetta) + yc;
    }

    // Проекции скорости отделения

    /**
     *   -m2
     * ------- * Lt * cos(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVX1(double Lt, double tetta_t) {
        return minusM2 * Lt * cos(tetta_t) + Vxc;
    }

    /**
     *   m1
     * ------- * Lt * cos(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVX2(double Lt, double tetta_t) {
        return plusM1 * Lt * cos(tetta_t) + Vxc;
    }

    /**
     *   -m2
     * ------- * Lt * sin(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVY1(double Lt, double tetta_t) {
        return minusM2 * Lt * sin(tetta_t) + Vyc;
    }

    /**
     *   m1
     * ------- * Lt * sin(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVY2(double Lt, double tetta_t) {
        return plusM1 * Lt * sin(tetta_t) + Vyc;
    }

    /**
     *    1
     * -------- * (m1 * x1 + m2 * x2)
     * m1 + m2
     */
    public double getXC(double x1, double x2) {
        return (m1 * x1 + m2 * x2) / (m1 + m2);
    }

    /**
     *    1
     * -------- * (m1 * y1 + m2 * y2)
     * m1 + m2
     */
    public double getYC(double y1, double y2) {
        return (m1 * y1 + m2 * y2) / (m1 + m2);
    }

    public double getCos_fi(double x1, double x2, double y1, double y2) {
        double xc = getXC(x1, x2);
        double yc = getYC(y1, y2);
        double rc = length(xc, yc);
        return xc / rc;
    }

    public double getSin_fi(double x1, double x2, double y1, double y2) {
        double xc = getXC(x1, x2);
        double yc = getYC(y1, y2);
        double rc = length(xc, yc);
        return yc / rc;
    }

    /**
     * x1 * cos(fi) + y1 * sin(fi)
     */
    public double getXN1(double x1, double x2, double y1, double y2) {
        double cos = getCos_fi(x1, x2, y1, y2);
        double sin = getSin_fi(x1, x2, y1, y2);
        return x1 * cos + y1 * sin;
    }

    /**
     * x2 * cos(fi) + y2 * sin(fi)
     */
    public double getXN2(double x1, double x2, double y1, double y2) {
        double cos = getCos_fi(x1, x2, y1, y2);
        double sin = getSin_fi(x1, x2, y1, y2);
        return x2 * cos + y2 * sin;
    }

    /**
     * -x1 * sin(fi) + y1 * cos(fi)
     */
    public double getYN1(double x1, double x2, double y1, double y2) {
        double cos = getCos_fi(x1, x2, y1, y2);
        double sin = getSin_fi(x1, x2, y1, y2);
        return -x1 * sin + y1 * cos;
    }

    /**
     * -x2 * sin(fi) + y2 * cos(fi)
     */
    public double getYN2(double x1, double x2, double y1, double y2) {
        double cos = getCos_fi(x1, x2, y1, y2);
        double sin = getSin_fi(x1, x2, y1, y2);
        return -x2 * sin + y2 * cos;
    }

//    /**
//     *      xc^2 + yc^2 - x1*xc-y1*yc
//     * K = --------------------------
//     *      x1*yc - y1*xc
//     *
//     * return arcsin(K/sqrt(1 + K^2))
//     */
//    public double getTetta(double x1, double x2, double y1, double y2) {
//        double xc = getXC(x1, x2);
//        double yc = getYC(y1, y2);
//
//        double first = pow(xc) + pow(yc) - (x1*xc  + y1*yc);
//        double second = x1*yc - y1*xc;
//
//        double k = first/second;
//
//        return Math.asin(k/sqrt(1 + pow(k)));
//    }

    /**
     * Возвращает радиус Земли.
     */
    public double getR3() {
        return R3;
    }

    /**
     * sqrt(x^2 + y^2)
     */
    public double length(double x, double y) {
        return sqrt(x * x + y * y);
    }

    /**
     * -K * x * m
     * -------------------
     * (sqrt(x^2 + y^2))^3
     */
    public double Gx(double x, double y, double m) {
        return (-K * x * m) / (cub(length(x, y)));
    }

    /**
     * -K * y * m
     * -------------------
     * (sqrt(x^2 + y^2))^3
     */
    public double Gy(double x, double y, double m) {
        return (-K * y * m) / (cub(length(x, y)));
    }

    /**
     *     length(x1 - x2, y1 - y2) - L
     * c * ---------------------------- , if > 0
     *                  L
     *
     * 0, otherwise
     */
    public double T(double x1, double y1, double x2, double y2, double L) {
        double kof = length(x1 - x2, y1 - y2) - L;
        double max = Math.max(kof, 0.0);
        return c * max / L;
    }

    /**
     *                                (x1 - x2)
     * T(x1, y1, x2, y2, L) * --------------------------
     *                         length(x1 - x2, y1 - y2)
     */
    public double Txy(double x1, double y1, double x2, double y2, double L) {
        return T(x1, y1, x2, y2, L) * (x1 - x2) / length(x1 - x2, y1 - y2);
    }

    /**
     *                                (y1 - y2)
     * T(x1, y1, x2, y2, L) * --------------------------
     *                         length(x1 - x2, y1 - y2)
     */
    public double Tyx(double x1, double y1, double x2, double y2, double L) {
        return T(x1, y1, x2, y2, L) * (y1 - y2) / length(x1 - x2, y1 - y2);
    }

    /**
     * Система развертывания с обратной связью.
     */
    public double Fc(double L, double Lt, double Lp, double Ltp) {
        double fc = doFc(L, Lt, Lp, Ltp);
        return Math.max(fc, Fcmin);
    }

    /**
     * Fcn(Lp, Ltp) + KL*(L - Lp) + KV*(Lt - Ltp)
     */
    public double doFc(double L, double Lt, double Lp, double Ltp) {
        return Fcn(Lp, Ltp) + KL * (L - Lp) + KV * (Lt - Ltp);
    }

    /**
     * Номинальная программа развертывания (бакалавриат).
     *
     *  m1*m2                                    Ltp
     * ------- * sigma^2 * ( a*(Lp - Lk) + b * ------ + cc * Lk )
     * m1 + m2                                  sigma
     */
    public double Fcn(double Lp, double Ltp) {
        return M1multM2_Om2 * (a * (Lp - Lk) + b * (Ltp / Om) + _3Lk);
    }

    // Уравнения развертывания

    /**
     *                                                                                    (m1 + m2)
     * Lp * ( (tetta_t_p + sigma)^2 - sigma^2 * (1 - 3 * cos(tetta_p)^2) - Fcn(Lp, Ltp) * ---------
     *                                                                                      m1*m2
     */
    public double L_tt_p(double Lp, double Ltp, double tetta_p, double tetta_t_p) {
        double first = Lp * (pow(tetta_t_p + Om) - Om2 * (1 - 3 * pow(cos(tetta_p))));
        double second = Fcn(Lp, Ltp) * M1plusM2;
        return first - second;
    }

    /**
     *       Ltp                          3
     * -2 * ---- * (tetta_t_p + sigma) - --- * sigma^2 * sin(2*tetta_p)
     *       Lp                           2
     */
    public double tetta_tt_p(double Lp, double Ltp, double tetta_p, double tetta_t_p) {
        double first = -2 * (Ltp / Lp) * (tetta_t_p + Om);
        double second = - 1.5 * Om2 * sin(2 * tetta_p);
        return first + second;
    }

    @Override
    public Vector diff(Vector vector) {
        if (vector.size() != getDimension()) {
            throw new RuntimeException("Размер вектора должен быть равен " + getDimension());
        }

        double x1 = vector.get(0);
        double y1 = vector.get(1);

        double Vx1 = vector.get(2);
        double Vy1 = vector.get(3);

        double x2 = vector.get(4);
        double y2 = vector.get(5);

        double Vx2 = vector.get(6);
        double Vy2 = vector.get(7);

        double L = vector.get(8);
        double L_t = vector.get(9);

        // suffix _p stands for programming

        double tetta_p = vector.get(10);
        double tetta_t_p = vector.get(11);

        double L_p = vector.get(12);
        double L_t_p = vector.get(13);

        double[] result = new double[getDimension()];

        result[0] = Vx1;
        result[1] = Vy1;
        result[2] = (Gx(x1, y1, m1) - Txy(x1, y1, x2, y2, L)) / m1;
        result[3] = (Gy(x1, y1, m1) - Tyx(x1, y1, x2, y2, L)) / m1;
        result[4] = Vx2;
        result[5] = Vy2;
        result[6] = (Gx(x2, y2, m2) + Txy(x1, y1, x2, y2, L)) / m2;
        result[7] = (Gy(x2, y2, m2) + Tyx(x1, y1, x2, y2, L)) / m2;
        result[8] = L_t;
        result[9] = (T(x1, y1, x2, y2, L) - Fc(L, L_t, L_p, L_t_p)) / m3;
        result[10] = tetta_t_p;
        result[11] = tetta_tt_p(L_p, L_t_p, tetta_p, tetta_t_p);
        result[12] = L_t_p;
        result[13] = L_tt_p(L_p, L_t_p, tetta_p, tetta_t_p);

        return new Vector(result);
    }

    @Override
    public int getDimension() {
        return 14;
    }

    private double pow(double value) {
        return value * value;
    }

    private double cub(double value) {
        return value * value * value;
    }

    public Func<Boolean, TimeVector> getExitFunction() {
        return timeVector -> {
            double l = timeVector.get(12);
            return l >= Lk - 5;
        };
    }
}
