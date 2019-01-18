import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil

List layouts = LayoutLocalServiceUtil.getLayouts(QueryUtil.ALL_POS, QueryUtil.ALL_POS)

layouts.stream().each { layout ->
	def properties = layout.getLayoutSet().getSettingsProperties()
	if (properties['javascript']) {
		println("${getUrlToLayout(layout)} <pre>${properties['javascript']}</pre>")
	}
}

def String getUrlToLayout(layout) {
	return "<a href='${PortalUtil.getLayoutActualURL(layout)}'>${layout.getGroup().getFriendlyURL()}${layout.getFriendlyURL()}</a>"
}
