-- CS 440 Spring 2020
-- HW 6 - Parser & parse trees for function calls like f(e), f(e1,e2), etc.

module HW_06_Fcall_Tests where
import HW_06_Fcall


tests = [
    parse_E " xx  ",
    parse_E "  xy + yz",
    parse_E "xy * yz",
    parse_E "xy * yz + a + b*c  ",
    parse_E "-x ",
    parse_E " --x",
    parse_E " - - x ",
    parse_E "ab+-cd",
    parse_E "(  w )  ",
    parse_E "(x+y*z+-w)",
    parse_E "((x))",
    parse_E "f(x)",
    parse_E "f(x,y)",
    parse_E "f(x+y,z)",
    parse_E "r + f(d, g(e, k*(ab+c)))",
    parse_E "star(plus(a,b), c, plus(d, e))"]

print (show tests)
