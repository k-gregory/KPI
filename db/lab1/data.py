class AttributeReference:
    def __init__(self, table, attribute, referer):
        self.table = table
        self.attribute = attribute
        self.referer = referer


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
                attr.reference = ref_mk(self)
        self.attributes = dict([(attr.name, attr) for attr in attributes])
        self.uniques = dict([(attr.name, set()) for attr in attributes if attr.unique])

    def reference(self, attribute_name):
        if attribute_name not in self.attributes:
            raise ValueError("Can't get reference to non-existent attribute")
        if attribute_name not in self.uniques:
            raise ValueError("Can't get reference to non-unique attribute")

        attr = self.attributes[attribute_name]

        def create_reference(referer):
            r = AttributeReference(self, attr, referer)
            attr.references.append(r)
            return r

        return create_reference

    def find(self, attr_name, value):
        if not attr_name in self.attributes:
            raise ValueError("Can't find by non-existent attribute")
        return [row for row in self.data if row[attr_name] == value]

    def add(self, **values):
        row = {}

        for attr in self.attributes.values():
            if attr.name in values:
                v = values[attr.name]
            else:
                v = attr.default

            if v is None and not attr.can_none:
                raise ValueError("Can't find apropriate value for {}".format(attr.name))

            ref = attr.reference
            if ref is not None:
                if len(ref.table.find(ref.attribute.name, v)) != 1:
                    raise ValueError("Can't reference {}".format(ref.attribute.name))

            if attr.unique:
                if v in self.uniques[attr.name]:
                    raise ValueError("Unique constraint fail: {}".format(attr.name))
                else:
                    self.uniques[attr.name].add(v)

            row[attr.name] = v

        self.data.append(row)
