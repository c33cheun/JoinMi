# Copyright 2016 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Auth for Application Default Credentials.
"""

from googlecloudsdk.calliope import base


@base.ReleaseTracks(base.ReleaseTrack.GA, base.ReleaseTrack.BETA)
class ApplicationDefault(base.Group):
  """Manage your active Application Default Credentials.

  Application Default Credentials provide a simple way to get authorization
  credentials for use in calling Google APIs. These commands assist in managing
  the active credentials on your machine that are used for testing purposes
  during local application development.

  These credentials are strictly used by Google client libraries in your own
  application. They are not used for any API calls made by the `gcloud` CLI, nor
  do they have any interaction with the credentials acquired by
  `gcloud auth login`.

  For more information on ADC and how they work, see:

    https://developers.google.com/identity/protocols/application-default-credentials
  """
  detailed_help = {
      'EXAMPLES': """\
          To create a service account and have your application use it for API
          access, run:

            $ gcloud iam service-accounts create my-account
            $ gcloud iam service-accounts keys create key.json
              --iam-account=my-account@my-project.iam.gserviceaccount.com
            $ export GOOGLE_APPLICATION_CREDENTIALS=key.json
            $ ./my_applicaiton.sh

          If you want your local application to temporarily use your own user
          credentials, run:

            $ {command} login

          which will take you through a web flow to acquire new user
          credentials.
          """,
  }
