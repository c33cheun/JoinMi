# Copyright 2015 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import json
import os
import sys

def _write_msg(**message):
    """Write a message to standard output.

    Args:
        **message: ({str: object, ...}) A JSON message encoded in keyword
            arguments.
    """
    json.dump(message, sys.stdout)
    sys.stdout.write('\n')
    sys.stdout.flush()


def error(message, *args):
    _write_msg(type='error', message=message % args)


def warn(message, *args):
    _write_msg(type='warn', message=message % args)


def info(message, *args):
    _write_msg(type='info', message=message % args)


def debug(message, *args):
    _write_msg(type='debug', message=message % args)


def print_status(message, *args):
    _write_msg(type='print_status', message=message % args)


def send_runtime_params(params):
    """Send runtime parameters back to the controller.

    Args:
        params: ({str: object, ...}) Set of runtime parameters.  Must be
            json-encodable.
    """
    _write_msg(type='runtime_parameters', runtime_data=params)


def get_config():
    """Request runtime parameters from the controller.

    Returns:
      (object) The runtime parameters represented as an object.
    """
    _write_msg(type='get_config')
    return dict_to_object(json.loads(sys.stdin.readline()))


def dict_to_object(json_dict):
    """Converts a dictionary to a python object.

    Converts key-values to attribute-values.

    Args:
      json_dict: ({str: object, ...})

    Returns:
      (object)
    """
    class Object(object):
        pass
    obj = Object()
    for name, val in json_dict.iteritems():
        if isinstance(val, dict):
          val = dict_to_object(val)
        setattr(obj, name, val)
    return obj


class RuntimeDefinitionRoot(object):
    """Abstraction that allows us to access files in the runtime definiton."""

    def __init__(self, path):
        self.root = path

    def read_file(self, *name):
        with open(os.path.join(self.root, *name)) as src:
            return src.read()


def gen_file(name, contents):
    """Generate the file.

    This writes the file to be generated back to the controller.

    Args:
        name: (str) The UNIX-style relative path of the file.
        contents: (str) The complete file contents.
    """
    _write_msg(type='gen_file', filename=name, contents=contents)
