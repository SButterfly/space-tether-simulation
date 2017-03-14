package com.sbutterfly.core.callbackTether;

import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.Vector;

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
    private final double cc;

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
    private final double K = 3.986 * cub(1000);

    // Высота орбиты (м)
    private final double H = 3 * 100_000;

    // Средний радиус Земли (м)
    private final double R3 = 6.371 * 1000_000;

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

    // ПРОЕКЦИИ СКОРОСТЕЙ
    private final double Vxc = 0.0;
    private final double Vyc = Vop;

    public CallbackTetherFunction(double m1, double m2, double m3,
                                  double KL, double KV,
                                  double a, double b, double cc,
                                  double Lk) {
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
        this.KL = KL;
        this.KV = KV;
        this.a = a;
        this.b = b;
        this.cc = cc;
        this.Lk = Lk;
    }

    /**
     *  -m2
     * ------- * L * cos(tetta) + xc
     * m1 + m2
     */
    public double getX1(double L, double tetta) {
        return (-m2/(m1 + m2)) * L * cos(tetta) + xc;
    }

    /**
     *    m1
     * ------- * L * cos(tetta) + xc
     * m1 + m2
     */
    public double getX2(double L, double tetta) {
        return (m1/(m1 + m2)) * L * cos(tetta) + xc;
    }

    /**
     *  -m2
     * ------- * L * sin(tetta) + yc
     * m1 + m2
     */
    public double getY1(double L, double tetta) {
        return (-m2/(m1 + m2)) * L * sin(tetta) + yc;
    }

    /**
     *    m1
     * ------- * L * sin(tetta) + yc
     * m1 + m2
     */
    public double getY2(double L, double tetta) {
        return (m1/(m1 + m2)) * L * sin(tetta) + yc;
    }

    // Проекции скорости отделения

    /**
     * -
     */
    public double getVxr(double Lt, double tetta_t) {
        return -Lt * cos(tetta_t);
    }

    /**
     * -Lt * cos(tetta_t)
     * Проекции скорости отделения
     */
    public double getVyr(double Lt, double tetta_t) {
        return -Lt * sin(tetta_t);
    }

    /**
     *   -m2
     * ------- * Lt * cos(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVX1(double Lt, double tetta_t) {
        return (-m2/(m1 + m2)) * Lt * cos(tetta_t) + Vxc;
    }

    /**
     *   m1
     * ------- * Lt * cos(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVX2(double Lt, double tetta_t) {
        return (m1/(m1 + m2)) * Lt * cos(tetta_t) + Vxc;
    }

    /**
     *   -m2
     * ------- * Lt * sin(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVY1(double Lt, double tetta_t) {
        return (-m2/(m1 + m2)) * Lt * sin(tetta_t) + Vyc;
    }

    /**
     *   m1
     * ------- * Lt * sin(tetta_t) + Vxc
     * m1 + m2
     */
    public double getVY2(double Lt, double tetta_t) {
        return (m1/(m1 + m2)) * Lt * sin(tetta_t) + Vyc;
    }

    /**
     * sqrt(x^2 + y^2)
     */
    private double length(double x, double y) {
        return sqrt(x * x + y * y);
    }

    /**
     * -K * x * m
     * -------------------
     * (sqrt(x^2 + y^2))^3
     */
    private double Gx(double x, double y, double m) {
        return (-K * x * m) / (cub(length(x, y)));
    }

    /**
     * -K * y * m
     * -------------------
     * (sqrt(x^2 + y^2))^3
     */
    private double Gy(double x, double y, double m) {
        return (-K * y * m) / (cub(length(x, y)));
    }

    /**
     *     length(x1 - x2, y1 - y2) - L
     * c * ---------------------------- , if > 0
     *                  L
     *
     * 0, otherwise
     */
    private double T(double x1, double y1, double x2, double y2, double L) {
        double kof = length(x1 - x2, y1 - y2) - L;
        if (kof >= 0) {
            return c * kof / L;
        } else {
            return 0;
        }
    }

    /**
     *                                (x1 - x2)
     * T(x1, y1, x2, y2, L) * --------------------------
     *                         length(x1 - x2, y1 - y2)
     */
    private double Txy(double x1, double y1, double x2, double y2, double L) {
        return T(x1, y1, x2, y2, L) * (x1 - x2) / length(x1 - x2, y1 - y2);
    }

    /**
     *                                (y1 - y2)
     * T(x1, y1, x2, y2, L) * --------------------------
     *                         length(x1 - x2, y1 - y2)
     */
    private double Tyx(double x1, double y1, double x2, double y2, double L) {
        return T(x1, y1, x2, y2, L) * (y1 - y2) / length(x1 - x2, y1 - y2);
    }

    /**
     * Система развертывания с обратной связью.
     */
    private double Fc(double L, double Lt, double Lp, double Ltp) {
        return doFc(L, Lt, Lp, Ltp);
    }

    /**
     * Fcn(Lp, Ltp) + KL*(L - Lp) + KV*(Lt - Ltp)
     */
    private double doFc(double L, double Lt, double Lp, double Ltp) {
        return Fcn(Lp, Ltp) + KL * (L - Lp) + KV * (Lt - Ltp);
    }

    /**
     * Номинальная программа развертывания (бакалавриат).
     *
     *  m1*m2                                    Ltp
     * ------- * sigma^2 * ( a*(Lp - Lk) + b * ------ * cc * Lk )
     * m1 + m2                                  sigma
     */
    private double Fcn(double Lp, double Ltp) {
        return (m1 * m2 / (m1 * m2)) * Om * Om * (a * (Lp - Lk) + b * (Ltp / Om) * cc * Lk);
    }

    // Уравнения развертывания

    /**
     *                                                                                    (m1 + m2)
     * Lp * ( (tetta_t_p + sigma)^2 - sigma^2 * (1 - 3 * cos(tetta_p)^2) - Fcn(Lp, Ltp) * ---------
     *                                                                                      m1*m2
     */
    private double L_tt_p(double Lp, double Ltp, double tetta_p, double tetta_t_p) {
        double first = Lp * (pow(tetta_t_p * Om) - Om * Om * (1 - 3 * pow(cos(tetta_p))));
        double second = Fcn(Lp, Ltp) * ((m1 + m2) / (m1 * m2));
        return first - second;
    }

    /**
     *       Ltp                          3
     * -2 * ---- * (tetta_t_p + sigma) - --- * sigma^2 * sin(2*tetta_p)
     *       Lp                           2
     */
    private double tetta_tt_p(double Lp, double Ltp, double tetta_p, double tetta_t_p) {
        double first = -2 * (Ltp / Lp) * (tetta_t_p + Om);
        double second = 1.5 * Om * Om * sin(2 * tetta_p);
        return first - second;
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
        double Lt = vector.get(9);

        // suffix _p stands for programming

        double tetta_p = vector.get(10);
        double tetta_t_p = vector.get(11);

        double L0_p = vector.get(12);
        double L_t_0_p = vector.get(13);

        Vector result = new Vector(getDimension());
        result.set(0, Vx1);
        result.set(1, Vy1);

        result.set(2, (Gx(x1, y1, m1) - Txy(x1, y1, x2, y2, L)) / m1);
        result.set(3, (Gy(x1, y1, m1) - Tyx(x1, y1, x2, y2, L)) / m1);

        result.set(4, Vx2);
        result.set(5, Vy2);

        result.set(6, (Gx(x2, y2, m2) - Txy(x1, y1, x2, y2, L)) / m2);
        result.set(7, (Gy(x2, y2, m2) - Tyx(x1, y1, x2, y2, L)) / m2);

        result.set(8, Lt);
        result.set(9, (T(x1, y1, x2, y2, L) - Fc(L, Lt, L0_p, L_t_0_p)) / m3);

        result.set(10, tetta_t_p);
        result.set(11, tetta_tt_p(L0_p, L_t_0_p, tetta_p, tetta_t_p));

        result.set(12, L_t_0_p);
        result.set(13, L_tt_p(L0_p, L_t_0_p, tetta_p, tetta_t_p));

        return result;
    }

    @Override
    public int getDimension() {
        return 14;
    }

    private double pow(final double value) {
        return value * value;
    }

    private double cub(double value) {
        return value * value * value;
    }
}
