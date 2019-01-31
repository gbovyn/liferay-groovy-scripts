import static javax.portlet.PortletRequest.RENDER_PHASE

import static com.liferay.journal.constants.JournalPortletKeys.JOURNAL
import com.liferay.journal.model.JournalArticle
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil
import com.liferay.portal.kernel.util.WebKeys
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil

import org.slf4j.LoggerFactory

LOGGER = LoggerFactory.getLogger("${getClass().name} - Replace in articles")

previewMode = true

PATTERN = '/group/guest'
NEW_VALUE = '/guest'
GROUP_ID = 10180

CONTEXT_SIZE = 30

List articles = JournalArticleLocalServiceUtil.getArticles(GROUP_ID)

Set latestArticles = articles.collect { JournalArticleLocalServiceUtil.getLatestArticle(it.resourcePrimKey) }

latestArticles.stream()
	.filter { article ->
		article.content.contains(PATTERN)
	}.each { article ->	
		println("Updating ${article.content.count(PATTERN)} occurrences in article ${getArticleHref(article)} ${previewMode ? '(preview)' : ''}")

		if (!previewMode) {
			article.with {
				LOGGER.info("Updating article ${articleId}")

				JournalArticleLocalServiceUtil.updateContent(
					groupId, articleId,
					version, content.replaceAll(PATTERN, NEW_VALUE)
				)
			}
		} else {
			def matches = article.content =~ /(?s)(?:.{$CONTEXT_SIZE})?(?:$PATTERN)(?:.{$CONTEXT_SIZE})?/
			matches.each { match ->
				println("Before: <pre><xmp>${match}</xmp></pre>")
				println("After: <pre><xmp>${match.replaceAll(PATTERN, NEW_VALUE)}</xmp></pre>")
			}
		}
	}

LOGGER.info('Done')

String getArticleHref(JournalArticle article) {
	return "<a href='${getArticleUrl(article)}'>${article.articleId}</a>"
}

String getArticleUrl(JournalArticle article) {
	def themeDisplay = actionRequest.getAttribute(WebKeys.THEME_DISPLAY)

	def url = PortletURLFactoryUtil.create(actionRequest, JOURNAL, themeDisplay.controlPanelLayout.plid, RENDER_PHASE)
	url.setParameter('mvcPath', '/edit_article.jsp')

	def fields = ['groupId', 'folderId', 'articleId', 'version']
	fields.each { field ->
		url.setParameter("${field}", "${article."${field}"}")
	}

	return url
}
