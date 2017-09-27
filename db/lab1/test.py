from lab1.data import Table, Attribute, autoinc

# Tables can have attributes
t = Table(Attribute("Check"))

# Tables default is used
v = Attribute("lol", default=autoinc)
t = Table(Attribute("some", default=autoinc()))
t.add(**{})
t.add(**{})
r = t.find()
assert len(r)==2
assert r[0]['some'] == 1
assert r[1]['some'] == 2

# Tables must have at least one attribute
try:
    t = Table()
    assert False
except ValueError:
    assert True

# Tables can't have duplicate attribute names
try:
    t = Table(Attribute("a"), Attribute("a"))
    assert False
except ValueError:
    assert True

# Tables can't give reference to non-existent field
try:
    t = Table(Attribute("foo"))
    r = t.reference("bar")
    assert False
except ValueError:
    assert True

# Tables can't give reference to non-unique field
try:
    t = Table(Attribute("foo", unique=False))
    r = t.reference("foo")
    assert False
except ValueError:
    assert True

# Tables can't bee searched by non-existent attributes
try:
    t = Table(Attribute("foo"))
    x = t.find(bar= 42)
    assert False
except ValueError:
    assert True

# Added rows can be found
t = Table(Attribute("x"), Attribute("y"))
t.add(x=1, y=3)
t.add(x=2, y=4)
l = t.find(x=2)
assert len(l) == 1
assert l[0]['y'] == 4


# Unique attribute can't be dublicated
try:
    t = Table(Attribute("foo", unique=True))
    t.add(foo=42)
    t.add(foo=42)
    assert False
except ValueError:
    assert True

# Reference existence is checked
try:
    a = Table(Attribute("foo", unique=True))
    b = Table(Attribute("a_foo", reference=a.reference("foo")))
    b.add(a_foo=42)
    assert False
except ValueError:
    assert True

# Reference existence is positively checked
a = Table(Attribute("foo", unique=True))
b = Table(Attribute("a_foo", reference=a.reference("foo")))
a.add(foo=42)
b.add(a_foo=42)


# Can't modify referenced row
t = Table(Attribute("foo", unique=True))
d = Table(Attribute("foo_ref", reference = t.reference("foo")))
t.add(foo=1)
t.add(foo=15)
d.add(foo_ref=1)
t.check_modify(t.find(foo=15)[0], t.attributes["foo"])
try:
    t.check_modify(t.find(foo=1)[0], t.attributes["foo"])
    assert False
except ValueError:
    assert True


# Update updates all needed rows
t = Table(Attribute("a"), Attribute("b"))
t.add(a=1, b=2)
t.add(a=1, b=4)
t.add(a=2, b=0)
t.update(t.find(a=1), b="updated")
r = t.find(a=1)
assert len(r) == 2
assert r[0]["b"] == r[1]["b"] == "updated"
assert t.find(a=2)[0]["b"] == 0


# Can't update referenced fields
t = Table(Attribute("foo", unique=True))
d = Table(Attribute("foo_ref", reference=t.reference("foo")))
t.add(foo=42)
t.update(t.find(foo=42), foo=24)
d.add(foo_ref=24)
try:
    t.update(t.find(foo=24), foo=1)
    assert False
except ValueError:
    assert True


# Can delete
t = Table(Attribute("foo"))
t.add(foo=1)
t.delete(t.find(foo=1))
assert len(t.find()) == 0

# Can't delete referenced
t = Table(Attribute("foo", unique=True))
d = Table(Attribute("lol", reference = t.reference("foo")))
t.add(foo=1)
t.add(foo=2)
d.add(lol=1)
d.add(lol=2)
d.delete(d.find(lol=2))
t.delete(t.find(foo=2))
try:
    t.delete(t.find(foo=1))
    assert False
except ValueError:
    assert True
