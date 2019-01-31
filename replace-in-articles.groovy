import com.liferay.journal.model.JournalArticle
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil

import org.slf4j.LoggerFactory

LOGGER = LoggerFactory.getLogger("${this.getClass().getName()} - Replace in articles")

previewMode = true

PATTERN = '/group/guest'
NEW_VALUE = '/guest'
GROUP_ID = 10180

CONTEXT_SIZE = 30

List articles = JournalArticleLocalServiceUtil.getArticles(GROUP_ID)

Set latestArticles = articles.collect { JournalArticleLocalServiceUtil.getLatestArticle(it.getResourcePrimKey()) } as Set

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
    return """/group/guest/~/control_panel/manage?p_p_id=com_liferay_journal_web_portlet_JournalPortlet\
&_com_liferay_journal_web_portlet_JournalPortlet_mvcPath=/edit_article.jsp\
&_com_liferay_journal_web_portlet_JournalPortlet_groupId=${article.groupId}\
&_com_liferay_journal_web_portlet_JournalPortlet_folderId=${article.folderId}\
&_com_liferay_journal_web_portlet_JournalPortlet_articleId=${article.articleId}\
&_com_liferay_journal_web_portlet_JournalPortlet_version=${article.version}"""
}
