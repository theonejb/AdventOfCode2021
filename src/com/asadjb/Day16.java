package com.asadjb;

import java.util.BitSet;

public class Day16 {
    static class AoCBitSet {
        BitSet bitSet;

        public AoCBitSet(BitSet bitSet) {
            this.bitSet = bitSet;
        }

        static AoCBitSet fromInput(String input) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                switch (input.charAt(i)) {
                    case '0':
                        sb.append("0000");
                        break;
                    case '1':
                        sb.append("0001");
                        break;
                    case '2':
                        sb.append("0010");
                        break;
                    case '3':
                        sb.append("0011");
                        break;
                    case '4':
                        sb.append("0100");
                        break;
                    case '5':
                        sb.append("0101");
                        break;
                    case '6':
                        sb.append("0110");
                        break;
                    case '7':
                        sb.append("0111");
                        break;
                    case '8':
                        sb.append("1000");
                        break;
                    case '9':
                        sb.append("1001");
                        break;
                    case 'A':
                        sb.append("1010");
                        break;
                    case 'B':
                        sb.append("1011");
                        break;
                    case 'C':
                        sb.append("1100");
                        break;
                    case 'D':
                        sb.append("1101");
                        break;
                    case 'E':
                        sb.append("1110");
                        break;
                    case 'F':
                        sb.append("1111");
                        break;
                }
            }
            String bits = sb.toString();

            BitSet bitSet = new BitSet(bits.length());
            for (int i = 0; i < bits.length(); i++) {
                if (bits.charAt(i) == '1')
                    bitSet.set(i);
            }

            return new AoCBitSet(bitSet);
        }

        String _toString(boolean partition) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < bitSet.length(); i++) {
                if (i % 4 == 0 && i > 0 && partition)
                    sb.append(' ');
                sb.append(bitSet.get(i) ? 1 : 0);
            }

            return sb.toString();
        }

        @Override
        public String toString() {
            return _toString(false);
        }

        String toHumanFormat() {
            return _toString(true);
        }

        int toInteger(int startingBit, int numberOfBits) {
            if (numberOfBits > 5) {
                throw new RuntimeException();
            }

            return Integer.parseInt(toBitString(startingBit, numberOfBits), 2);
        }

        String toBitString(int startingBit, int numberOfBits) {
            StringBuilder bitString = new StringBuilder();
            for (int i = startingBit - 1; i < startingBit - 1 + numberOfBits; i++) {
                bitString.append(bitSet.get(i) ? '1' : '0');
            }

            return bitString.toString();
        }
    }

    static class BITSParser {
        AoCBitSet bitSet;
        int pos = 1;

        public BITSParser(AoCBitSet bitSet) {
            this.bitSet = bitSet;
        }

        int getVersion() {
            return bitSet.toInteger(pos, 3);
        }

        int getTypeId() {
            return bitSet.toInteger(pos + 3, 3);
        }

        BITSValuePacket parseValuePacket() {
            int version = getVersion(),
                    typeId = getTypeId(),
                    startingPos = pos + 6;
            StringBuilder bits = new StringBuilder();

            while (true) {
                String nextBits = bitSet.toBitString(startingPos, 5);
                bits.append(nextBits.substring(1));
                startingPos += 5;
                if (nextBits.charAt(0) == '0') break;
            }
            pos = startingPos;

            return new BITSValuePacket(version, typeId, Long.parseLong(bits.toString(), 2));
        }

        BITSOperatorPacket parseOperatorPacket() {
            int version = getVersion(),
                    type = getTypeId(),
                    startingPos = pos + 6;

            int bitLabel = bitSet.toInteger(startingPos, 1);
            startingPos++;

            return new BITSOperatorPacket();
        }
    }

    static abstract class BITSPacket {
    }

    static class BITSValuePacket extends BITSPacket {
        int version, type;
        long value;

        public BITSValuePacket(int version, int type, long value) {
            this.version = version;
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("V: %d T: %d VAL: %d", version, type, value);
        }
    }

    static class BITSOperatorPacket extends BITSPacket {

    }

    static void main() {
        String input = Utils.readInput("./day16.txt").get(0);
        AoCBitSet bitSet = AoCBitSet.fromInput(input);

        part1(bitSet);
    }

    static void part1(AoCBitSet bitSet) {
        System.out.println(bitSet.toHumanFormat());

        BITSParser parser = new BITSParser(bitSet);
        BITSValuePacket packet = parser.parseValuePacket();
        System.out.println(packet);
        System.out.println(parser.pos);
    }
}
