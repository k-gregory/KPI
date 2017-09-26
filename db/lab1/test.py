from lab1.data import Table, Attribute

# Tables can have attributes
t = Table(Attribute("Check"))

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
    x = t.find("bar", 42)
    assert False
except ValueError:
    assert True

# Added rows can be found
t = Table(Attribute("x"), Attribute("y"))
t.add(x=1, y=3)
t.add(x=2, y=4)
l = t.find('x', 2)
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