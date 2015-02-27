package com.sbutterfly.core.rope;

import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Sergei on 26.02.2015.
 */
public class RopeFunction extends Function {

    private double m1;
    private double m2;

    private double p;

    private double a;
    private double b;

    private double Lk;

    private double m;

    private double K = 398600.02;
    private double R = 6371;
    private double Om = Math.sqrt(K / (R * R * R));
    private double Om2 = Om * Om;
    private double sinO;
    private double cosO;
    private double sinB;
    private double cosB;
    private double sin2O;
    private double sin2B;


    public RopeFunction(double m1, double m2, double p, double a, double b, double lk) {
        this.m1 = m1;
        this.m2 = m2;
        this.p = p;
        this.a = a;
        this.b = b;
        this.Lk = lk;
        this.m = m1 + m2;
    }

    private double V(double L) {
        return (m1 - L * p) * (m2 - L * p / 2d) / m;
    }

    private double M(double L) {
        return (m1 - L * p) * (m2 + L * p) / m;
    }

    private double R(double L, double Lt) {
        return p * pow(Lt) * (m1 - m2 - 2 * L * p) / (2 * m);
    }

    private double J(double L) {
        return pow(L) * (12 * m1 * m2 - 8 * L * p * m2 + 4 * L * p * m1 - 3 * pow(L * p)) / (12 * m);
    }

    private double T(double L, double Lt) {
        return V(L) * Om2 * (a * (L - Lk) + b * Lt / Om + 3 * Lk);
    }

    private double Ltt(double Lt, double L, double Ot, double O, double Bt, double B) {
        return V(L) / M(L) * L * (pow(Ot) + 2 * Ot * Om * cosB + pow(Bt * cosO) - pow(Om * cosO * sinB) + Om * Bt * sinB * sin2O + 3 * pow(Om * cosO * cosB)) - (T(L, Lt) + R(L, Lt)) / M(L);
    }

    private double Ott(double Lt, double L, double Ot, double O, double Bt, double B) {
        return -2 * V(L) / J(L) * L * Lt * (Ot + Om * cosB) + Om2 * sinO * cosO * pow(sinB) - pow(Bt) * sinO * cosO + 2 * Om * Bt * pow(cosO) * sinB - 3d / 2d * Om2 * sin2O * pow(cosB);
    }

    private double Btt(double Lt, double L, double Ot, double O, double Bt, double B) {
        return 1 / pow(cosO) * (-2 * V(L) / J(L) * L * Lt * (Bt * pow(cosO) + Om * sin2O * sinB) + Ot * Bt * sin2O - 2 * Om * Ot * pow(cosO) * sinB - 2 * Om2 * pow(cosO * L) * sin2B);
    }

    @Override
    public synchronized Vector Diff(Vector x) {
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

        sin2O = sin(2 * O);
        sin2B = sin(2 * B);

        final double v2 = Ltt(Lt, L, Ot, O, Bt, B);
        final double v4 = Ott(Lt, L, Ot, O, Bt, B);
        final double v6 = Btt(Lt, L, Ot, O, Bt, B);
        return new Vector(Lt, v2, Ot, v4, Bt, v6);
    }

    private double pow(double value) {
        return value * value;
    }
}
