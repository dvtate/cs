from app.flightBookerDB import db_interface
from string import ascii_letters, digits
from random import choice
import hashlib
import sys


# do not change this!
pw_salt = "Zm1ha3NsZW5kZmlsIGFlZ2Z1YWhrdXMgZm5qa3NkbmtqdmMgYW5zY3Nham5kY2FucyBkc2puY3NhamRrbmZqbjRxZm52cjM5NA=="

# authtoken == 64 random letters/numbers
def generateToken(customerId):
    return str(customerId) + ''.join([choice(ascii_letters + digits) for i in range(32)])


# sha512: salt,customerId,password
def hashPassword(customerId, password):
    return hashlib.sha512(','.join([pw_salt, str(customerId), password]).encode('utf-8')).hexdigest()


def checkUserCreds(email, password):
    c = db_interface.conn.cursor()
    c.execute('SELECT customerId, password FROM customers WHERE email = %s', (email, ))
    r = c.fetchone()
    print(r, flush=True)
    c.close()
    return r[0] if hashPassword(r[0], password) == r[1] else False


def loginUser(email, password):
    customerId = checkUserCreds(email, password)
    if (not customerId):
        return False
    c = db_interface.conn.cursor()
    token = generateToken(customerId)
    c.execute('UPDATE customers set authToken=%s WHERE email=%s;', (token, email ))
    c.close()
    return token


def authUser(headers):
    try:
        # comes in form `Authorization: Bearer 4u3ur34hf4n3hf4mqru8fj3rfh4397479t`
        # https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization
        authToken = headers.get('Authorization').split(' ')[1]
        c = db_interface.conn.cursor()
        c.execute('SELECT customerId FROM customers WHERE authToken= %s ', (authToken, ))
        id = c.fetchone()
        c.close()
        id = id[0]
        return id
    except Exception as e:
        print(e, flush=True)
        return False
