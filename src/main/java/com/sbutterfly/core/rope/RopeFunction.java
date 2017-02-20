package com.sbutterfly.core.rope;

import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Sergei on 26.02.2015.
 * Функция моделирования развертки КА
 */
public class RopeFunction extends Function {

    private final double m1;
    private final double m2;

    private final double p;

    private final double a;
    private final double b;

    private final double Lk;

    private final double m;

    private final double K = 398600.02;
    private final double R = 6371;
    private final double Om;
    private final double Om2;

    private double sinO;
    private double cosO;
    private double sinB;
    private double cosB;
    private double sin2O;
    private double sin2B;

    public RopeFunction(double m1, double m2, double p, double a, double b, double lk, double h) {
        this.m1 = m1;
        this.m2 = m2;
        this.p = p;
        this.a = a;
        this.b = b;
        this.Lk = lk;
        this.m = m1 + m2;
        this.Om = Math.sqrt(K / ((R + h) * pow(R + h)));
        this.Om2 = Om * Om;
    }

    private double V(final double L) {
        return (m1 - L * p) * (m2 - L * p / 2d) / m;
    }

    private double M(final double L) {
        return (m1 - L * p) * (m2 + L * p) / m;
    }

    private double R(final double L, final double Lt) {
        return p * pow(Lt) * (m1 - m2 - 2d * L * p) / (2d * m);
    }

    private double J(final double L) {
        return pow(L) * (12d * m1 * m2 - 8d * L * p * m2 + 4d * L * p * m1 - 3d * pow(L * p)) / (12d * m);
    }

    public double T(final double L, final double Lt) {
        return V(L) * Om2 * (a * (L - Lk) + b * Lt / Om + 3d * Lk);
    }

    private double Ltt(final double Lt, final double L, final double Ot, final double O, final double Bt, final double B) {
        return V(L) / M(L) * L * (pow(Ot) + 2 * Ot * Om * cosB + pow(Bt * cosO) - pow(Om * cosO * sinB) + Om * Bt * sinB * sin2O + 3d * pow(Om * cosO * cosB)) - (T(L, Lt) + R(L, Lt)) / M(L);
    }

    private double Ott(final double Lt, final double L, final double Ot, final double O, final double Bt, final double B) {
        return -2d * V(L) / J(L) * L * Lt * (Ot + Om * cosB) + Om2 * sinO * cosO * pow(sinB) - pow(Bt) * sinO * cosO + 2d * Om * Bt * pow(cosO) * sinB - 1.5 * Om2 * sin2O * pow(cosB);
    }

    private double Btt(final double Lt, final double L, final double Ot, final double O, final double Bt, final double B) {
        return 1d / pow(cosO) * (-2d * V(L) / J(L) * L * Lt * (Bt * pow(cosO) + Om * sin2O * sinB) + Ot * Bt * sin2O - 2d * Om * Ot * pow(cosO) * sinB - 2d * Om2 * pow(cosO * L) * sin2B);
    }

    @Override
    public synchronized Vector diff(Vector x) {
        if (x.size() != 6) {
            throw new RuntimeException("Размер вектора должен быть равен 6.");
        }

        final double L = x.get(0);
        final double Lt = x.get(1);
        final double O = x.get(2);
        final double Ot = x.get(3);
        final double B = x.get(4);
        final double Bt = x.get(5);

        sinO = sin(O);
        cosO = cos(O);
        sinB = sin(B);
        cosB = cos(B);

        sin2O = sin(2d * O);
        sin2B = sin(2d * B);

        final double v2 = Ltt(Lt, L, Ot, O, Bt, B);
        final double v4 = Ott(Lt, L, Ot, O, Bt, B);
        final double v6 = Btt(Lt, L, Ot, O, Bt, B);
        return new Vector(Lt, v2, Ot, v4, Bt, v6);
    }

    private double pow(final double value) {
        return value * value;
    }
}
