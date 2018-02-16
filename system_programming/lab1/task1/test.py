import unittest
import subprocess as sp
import os
from stat import ST_MODE, ST_ATIME, ST_MTIME, ST_CTIME, ST_SIZE
from pathlib import Path

def overwrite(filename, content):
    with open(filename, 'w') as f:
        f.write(content)

def simple_read(filename):
    with open(filename, 'r') as f:
        return f.read()

def call_prog(*args):
    def decode_out(output):
        return output.decode('utf-8').strip()

    proc = sp.Popen(['./build/main', *args], stdout=sp.PIPE, stderr=sp.PIPE)
    (stdout, stderr) = proc.communicate(timeout=10)
    (stdout, stderr) = (decode_out(stdout), decode_out(stderr))

    returncode = proc.returncode
    return (returncode, stdout, stderr)

class TestFileCopying(unittest.TestCase):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.in_filename = 'build/will-be-overwritten'
        self.out_filename = 'build/will-be-removed'
        self.same_filename = 'build/will-be-overwritten2'
        self.link_src = 'will-be-overwritten2' #depends on same_filename
        self.nonexist_filename = '/does/not/exist'
        self.link_filename = 'build/link'

        assert not os.path.exists(self.nonexist_filename)


    def setUp(self):
        for f in [self.in_filename, 
                self.out_filename, 
                self.same_filename,
                self.link_filename]:
            try:
                os.remove(f)
            except FileNotFoundError:
                pass


    def test_args_count(self):
        for args in [(), (self.in_filename,)]:
            with self.subTest(args=args):
                (code, out, err) = call_prog(*args)
                self.assertEqual(code, 1)
                self.assertEqual(out, '')
                self.assertIn('Usage', err)

    def test_nonexistent_input_files_fail(self):
        (code, out, err) = call_prog('/does/not/exist', self.out_filename)
        self.assertEqual(code, 1)
        self.assertEqual(out, '')
        self.assertIn('No such file', err)

    def test_existent_file_not_removed(self):
        overwrite(self.in_filename, 'dummy')
        overwrite(self.out_filename, 'other dummy')
        os.chmod(self.out_filename, 0o220)

        pre_run_stat = list(os.stat(self.out_filename))
        (code, out, err) = call_prog(self.in_filename, self.out_filename)
        post_run_stat = list(os.stat(self.out_filename))
        for key in [ST_ATIME, ST_CTIME, ST_MTIME, ST_SIZE]:
            post_run_stat[key] = pre_run_stat[key]

        self.assertEqual(pre_run_stat, post_run_stat)
        self.assertEqual(code, 0)
        self.assertEqual(err, '')
        self.assertIn('Wrote', out)


    def test_file_created(self):
        overwrite(self.in_filename, self.out_filename)
        (return_code, out, err) = call_prog(self.in_filename, self.out_filename)
        file_exists = Path(self.out_filename).is_file()
        file_mode = os.stat(self.in_filename)[ST_MODE]

        self.assertEqual(return_code, 0)
        self.assertTrue(file_exists)
        self.assertEqual(file_mode, 0o100644)
        self.assertEqual(err, '')
        self.assertIn('Wrote', out);

    def test_src_dest_name_must_differ(self):
        overwrite(self.same_filename, 'some text')

        (code, out, err) = call_prog(self.same_filename, self.same_filename)

        self.assertEqual(code, 1)
        self.assertEqual(out, '')
        self.assertIn('different files', err)

    def test_symlinks_must_differ(self):
        overwrite(self.same_filename, 'some dummy')
        os.symlink(self.link_src, self.link_filename)

        (code, out, err) = call_prog(self.same_filename, self.link_filename)

        self.assertEqual(code, 1)
        self.assertEqual(out, '')
        self.assertIn('different files', err)

    def test_hardlinks_must_differ(self):
        overwrite(self.same_filename, 'some dummy')
        os.link(self.same_filename, self.link_filename)

        (code, out, err) = call_prog(self.same_filename, self.link_filename)

        self.assertEqual(code, 1)
        self.assertEqual(out, '')
        self.assertIn('different files', err)

    def assertRunWithText(self, text):
        overwrite(self.in_filename, text)

        (code, out, err) = call_prog(self.in_filename, self.out_filename)
        res_content = simple_read(self.out_filename)

        self.assertEqual(code, 0)
        self.assertEqual(err, '')
        self.assertIn('Wrote', out)
        self.assertIn(str(len(text)), out)
        self.assertEqual(text.lower(), res_content)


    def test_content_written(self):
        content = 'QWerTYU\tiOpjk\ndASdFGhKLXCvbnrTYI\0sdEWRdfDRwErVRwerFSrre\n'
        content = content * 20

        assert len(content) > 1024

        for i in range(0, len(content)):
            text = content[:i]
            with self.subTest(content=text):
                self.assertRunWithText(text)

if __name__ == '__main__':
    unittest.main()
