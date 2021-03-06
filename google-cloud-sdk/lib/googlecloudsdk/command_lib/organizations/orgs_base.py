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

"""Base class for Organization commands."""

from googlecloudsdk.calliope import base
from googlecloudsdk.core import apis
from googlecloudsdk.core import resources


ORGS_COLLECTION = 'cloudresourcemanager.organizations'
ORGS_API_VERSION = 'v1beta1'


class OrganizationCommand(base.Command):
  """Common methods for an organization command."""

  def Collection(self):
    return ORGS_COLLECTION

  def OrganizationsClient(self):
    client = apis.GetClientInstance('cloudresourcemanager', 'v1beta1')
    return client.organizations

  def OrganizationsMessages(self):
    return apis.GetMessagesModule('cloudresourcemanager', 'v1beta1')

  def GetOrganizationRef(self, organization_id):
    # This is fun stuff here.
    # Gcloud's apitools client generator doesn't support one platform {+name}
    # URL template patterns. Autogenerated ID field is the collection name
    # folowed by "Id", e.g. "organizationsId". Our API
    # already has an "organizationId" field that's deprecated and was used
    # before we switched to the one platform "name" convention.
    # "organizationsId=123" gets turned into "name=organizations/123" under the
    # hood in the client. We don't pass organizationId because it's deprecated.
    registry = resources.REGISTRY.Clone()
    registry.RegisterApiByName('cloudresourcemanager', ORGS_API_VERSION)
    return registry.Parse(
        None,
        params={
            'organizationsId': organization_id,
            'organizationId': organization_id
        },
        collection=self.Collection())

  def GetUriFunc(self):
    def _GetUri(resource):
      return self.GetOrganizationRef(resource.name[len(
          'organizations/'):]).SelfLink()
    return _GetUri
