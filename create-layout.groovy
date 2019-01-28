import com.liferay.portal.kernel.model.LayoutConstants
import com.liferay.portal.kernel.service.GroupLocalServiceUtil
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil
import com.liferay.portal.kernel.service.UserLocalServiceUtil
import com.liferay.portal.kernel.service.ServiceContext
import com.liferay.portal.kernel.util.PortalUtil

long companyId = PortalUtil.getDefaultCompanyId()

long userId = UserLocalServiceUtil.fetchUserByScreenName(companyId, "Guest").getUserId()
long groupId = GroupLocalServiceUtil.fetchGroup(companyId, "Guest").getGroupId()
boolean privateLayout = false
long parentLayoutId = 0
String name = "New page"
String title = "New page title"
String description = "Created programmatically"
String type = LayoutConstants.TYPE_PORTLET
boolean hidden = true
String friendlyURL = "/new-page"
ServiceContext serviceContext = new ServiceContext()

LayoutLocalServiceUtil.addLayout(userId, groupId, privateLayout, parentLayoutId, name, title, description, type, hidden, friendlyURL, serviceContext)
