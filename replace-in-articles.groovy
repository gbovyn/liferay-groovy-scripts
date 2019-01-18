import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil

final PATTERN = '/group/guest'
final LOCALES = ['nl_BE', 'fr_FR']
final GROUP_ID = 10180

List articles = JournalArticleLocalServiceUtil.getArticles(GROUP_ID)

LOCALES.each { locale ->
	articles.stream()
		.map {
			['article': it, 'content': it.getContentByLocale(locale)]
		}
		.filter {
			it.content.with { contains(locale) && contains(PATTERN) }
		}.each {
			println("Updating article ${it.article.getArticleId()} (${locale})")

			JournalArticleLocalServiceUtil.updateContent(
				it.article.getGroupId(), it.article.getArticleId(), 
				it.article.getVersion(), it.content.replace(PATTERN, '/')
			)
		}
}
