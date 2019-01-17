import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil

List layouts = LayoutLocalServiceUtil.getLayouts(QueryUtil.ALL_POS, QueryUtil.ALL_POS)

layouts.stream().each { layout ->
	layout.getLayoutType().getPortlets().stream().each { portlet ->
		if (portlet.getPortletName().contains("PortletName")) {
			println(PortalUtil.getLayoutFullURL(layout.getGroup().getGroupId(), portlet.getPortletId()))
		}
	}
}
