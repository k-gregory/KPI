import requests
import sys
from lxml import etree as ET
import pandas


if len(sys.argv) != 3:
    sys.exit('Specify category name and output file name')

category_name = sys.argv[1]
root_name = 'Category:' + category_name


API_URL = 'https://en.wikipedia.org/w/api.php'
MAX_SUBCATEGORIES = 2
MAX_ARTICLES = 2
MAX_DOWNLOAD_ARTICLES = 2


def chunks(l, n):
    """Yield successive n-sized chunks from l."""
    for i in range(0, len(l), n):
        yield l[i:i + n]

def make_subcategory_query(category_name):
    return {
        'action': 'query',
        'format': 'xml',
        'list': 'categorymembers',
        'cmtitle': 'Category:' + category_name,
        'cmtype': 'subcat',
        'cmlimit': MAX_SUBCATEGORIES
    }


def make_subcategory_articles_query(category_pageid):
    return {
        'action': 'query',
        'format': 'xml',
        'list': 'categorymembers',
        'cmpageid': category_pageid,
        'cmtype': 'page',
        'cmlimit': MAX_ARTICLES
    }


root_category_articles_query = {
    'action': 'query',
    'format': 'xml',
    'list': 'categorymembers',
    'cmtitle': root_name,
    'cmtype': 'page',
    'cmlimit': MAX_ARTICLES
}

def make_pages_query(page_ids):
    id_list = '|'.join([str(x) for x in page_ids])
    return {
        'action': 'query',
        'format': 'xml',
        'prop': 'revisions|categories',
        'pageids': id_list,
        'rvprop': 'content'
    }


def get_page_ids(category_name):
    subcategories_data = requests.get(API_URL, params=make_subcategory_query(category_name))
    subcategories = ET.fromstring(subcategories_data.content)

    for subcategory in subcategories.xpath('/api/query/categorymembers/cm'):
        subcategory_title = subcategory.get('title')
        subcategory_id = subcategory.get('pageid')
        subcategory_page_data = requests.get(API_URL, params=make_subcategory_articles_query(subcategory_id))
        subcategory_page = ET.fromstring(subcategory_page_data.content)
        for article_id in subcategory_page.xpath('/api/query/categorymembers/cm/@pageid'):
            yield (subcategory_title, article_id)

    direct_articles_data = requests.get(API_URL, params=root_category_articles_query)
    direct_articles = ET.fromstring(direct_articles_data.content)
    for article_id in direct_articles.xpath('/api/query/categorymembers/cm/@pageid'):
        yield (root_name, article_id)


def download_pages(page_ids):
    for ids_chunk in chunks(page_ids, MAX_DOWNLOAD_ARTICLES):
        page_data = requests.get(API_URL, params=make_pages_query(ids_chunk))
        page = ET.fromstring(page_data.content)
        yield from page.xpath('/api/query/pages/page')


data = list(get_page_ids(category_name))
df = pandas.DataFrame(data)
unique_page_ids =df[1].unique()
by_category = df.groupby(0)

#Write pages to file
xml_root = ET.Element('pages')
for page in download_pages(unique_page_ids):
    xml_root.append(page)
with open(sys.argv[2], 'wb') as f:
    f.write(ET.tostring(xml_root, pretty_print=True))