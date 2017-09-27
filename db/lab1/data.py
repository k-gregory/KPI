class Seq:
    def __init__(self):
        self.s = 0

    def __call__(self):
        self.s += 1
        return self.s

def autoinc():
    return Seq()

class AttributeReference:
    def __init__(self, table, attribute, referer, ref_attr):
        self.table = table
        self.attribute = attribute
        self.referer = referer
        self.ref_attr = ref_attr


class Attribute:
    def __init__(self, name, can_none=True, reference=None, default=None, unique=False):
        self.name = name
        self.reference = reference
        self.default = default
        self.references = []
        self.can_none = can_none
        self.unique = unique

    def __str__(self):
        if self.reference is not None:
            return "'{}, refers {}".format(self.name, self.reference)
        else:
            return "'{}".format(self.name)


class Table:
    @staticmethod
    def check_attributes(attributes):
        if len(attributes) < 1:
            raise ValueError("Table must have at least one attribute")

        attr_names = set()
        for attr in attributes:
            if attr.name in attr_names:
                raise ValueError("Duplicate attribute name")
            else:
                attr_names.add(attr.name)

    def __init__(self, *attributes):
        Table.check_attributes(attributes)
        self.data = []

        for attr in attributes:
            ref_mk = attr.reference
            if ref_mk is not None:
                attr.reference = ref_mk(self, attr.name)
        self.attributes = dict([(attr.name, attr) for attr in attributes])
        self.uniques = dict([(attr.name, set()) for attr in attributes if attr.unique])

    def reference(self, attribute_name):
        if attribute_name not in self.attributes:
            raise ValueError("Can't get reference to non-existent attribute")
        if attribute_name not in self.uniques:
            raise ValueError("Can't get reference to non-unique attribute")

        attr = self.attributes[attribute_name]

        def create_reference(referer, ref_attr):
            r = AttributeReference(self, attr, referer, ref_attr)
            attr.references.append(r)
            return r

        return create_reference

    def check_modify(self, mrow, attribute):
        for ref in attribute.references:
            for row in ref.referer.data:
                if ref.ref_attr in row and attribute.name in mrow:
                    if row[ref.ref_attr] == mrow[attribute.name]:
                        raise ValueError("Can't modify {}, dependent row exists".format(attribute))

    def find(self, **conds):
        for k in conds:
            if not k in self.attributes:
                raise ValueError("Can't find by non-existent attribute")

        rs = []
        for row in self.data:
            matches = True
            for k,v in conds.items():
                if row[k] != v:
                    matches = False
                    break
            if matches:
                rs.append(row)

        return rs


    def __update_row(self, row, updates):
        for attr in self.attributes.values():
            if not attr.name in updates:
                continue
            else:
                v = updates[attr.name]

            if v is None and not attr.can_none:
                raise ValueError("Can't put None to {}".format(attr.name))

            ref = attr.reference
            if ref is not None:
                if len(ref.table.find(**{ref.attribute.name:v})) != 1:
                    raise ValueError("Can't reference {}".format(ref.attribute.name))

            if attr.unique:
                if v in self.uniques[attr.name]:
                    if attr.name in row and row[attr.name] == v:
                        pass
                    else:
                        raise ValueError("Unique constraint fail: {}".format(attr.name))
                else:
                    if attr.name in row:
                        self.uniques[attr.name].remove(row[attr.name])
                    self.uniques[attr.name].add(v)

            self.check_modify(row, attr)
            row[attr.name] = v

    def delete(self, rows):
        for row in rows:
            for attr in self.attributes.values():
                self.check_modify(row, attr)
                if attr.unique:
                    self.uniques[attr.name].remove(row[attr.name])

        #WHYYY???
        self.data = list(filter(lambda r: r not in rows, self.data))

    def update(self, rows, **changes):
        for row in rows:
            self.__update_row(row, changes)

    def add(self, **values):
        row = {}

        for attr in self.attributes.values():
            if attr.name not in values:
                values[attr.name] = attr.default()


        self.__update_row(row, values)
        self.data.append(row)
